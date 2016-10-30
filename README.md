# PosterByCleanArchitecture
*写在前面的话：最近正在写一个自己的开源项目，打算使用像rxjava，retrofit，dagger2等开源框架，结果一个多月过去了，大框架基本写好了，却因为布局想绚丽一点，一直都没确定，最后拖延症犯了，就没再写了，这是多么痛的领悟...*![](http://cdn.aixifan.com/dotnet/20130418/umeditor/dialogs/emotion/images/ac/01.gif)

*另一方面，因为自带囫囵吞枣“技能”，导致学习新技术的时候一目十行，结果只是照猫画虎而已，并没有习得精髓，因此也促使我想开个技术博客，一方面梳理自己的思绪，另一方面希望这个行为能够治治自己的拖延症、纠结症等等等等<del>，说得跟真的一样。</del>*![](http://cdn.aixifan.com/dotnet/20130418/umeditor/dialogs/emotion/images/ac2/21.gif)



----------

**什么是干净架构**

  咳咳，言归正传，由于我的项目是参照干净架构的架构模式来写的，因此，先讲讲干净架构吧。

  干净架构英文是clean architecture，也有翻译为清晰架构的，都给人结构分明的感觉，然而翻译成洗白白架构的筒子请去走廊罚站。

  这种架构最初是由uncle-bob提出来的，啰啰嗦嗦的名词解释我就不复制黏贴了，筒子们可以看我后面贴出来的博文链接详细看，我就说说我的理解：

  干净架构是一种分层的架构方式，将核心业务（对应domain层）、UI相关（对应presenter层）以及数据加载（对应data层）彼此独立开来，不同的层之间由接口依次连接起来，但却又彼此不了解彼此的具体实现。

  大概就是这种感觉：

![](https://8thlight.com/blog/assets/posts/2012-08-13-the-clean-architecture/CleanArchitecture-8b00a9d7e2543fa9ca76b81b05066629.jpg)

  我想大多数第一次接触这个概念以及看到这个图的人跟我一样，都是黑人问号脸.jpg的。侦察到大神向我投来鄙视的目光，淡定装作看不到。![](http://cdn.aixifan.com/dotnet/20130418/umeditor/dialogs/emotion/images/ac2/42.gif)

  举个通俗的例子的吧，双11不是要到了么，假设某大学三个童鞋<del>和尚</del>打算出一版专题海报，抨击这个本应抱团取暖的节日渐渐演变成购物的狂欢节所折射出来的社会浮躁，为充满铜臭、酸臭味的空气注入一股清香。

  起初，三个童鞋共同绘制海报，每人绘制一部分，结果绘制出来的海报没有层次、风格不一，简直就是一坨翔<del>，说得就是你，什么都自己来的activity</del>。后来他们痛定思痛，决定根据每个人的特长进行分工，a童鞋擅长分析（domain），因此由a负责整版海报内容的构思；b童鞋擅长搜集资料（data），因此由b负责提供海报的数据；c同学擅长绘画（presenter），因此由c负责绘制海报。整个过程大概是这种感觉：

  ![image](C:/Users/wind/Desktop/流程.png)

  这种分工使得彼此职责明确，哪个部分出问题了只需要修改相应部分即可。比如说，b有愧于老司机的名号，搜集的资料给人“裤子都脱了，就给我看这”的感觉，那么只需要痛打他一顿，让他重新定义搜索公式啥的即可，并不影响a和c。这也是干净架构所传递出来的模块间解耦的思想。

  扯了那么多，是否对干净架构有了初步的了（wu）解呢？接下来，让我们更深入一些吧。![](http://cdn.aixifan.com/dotnet/20130418/umeditor/dialogs/emotion/images/ac2/29.gif)



----------
**<h3>domain层</h3>**


  domain层在干净架构里被定位为业务逻辑层，这一层是纯java层，不涉及android相关的jar包，这也就是为什么干净架构易于测试的原因。要理解这一层，首先要思考何为业务，我曾经认为一个动作就是一个业务，例如，从网络上请求新闻列表和博客列表是两个动作，也是两个业务，因为他们请求的参数和返回数据的javaBean都不一样，但后来发现一些问题，比如说这样写代码重复性很大，各个业务类之间不同的仅仅是一个泛型而已，而且由于带上的泛型有可能是实现了parcelable接口的，这样的话就引入了android的jar包，违背了上面的原则。因此，我认为一种动作对应一个业务类，也就是说，请求新闻列表和请求博客列表都是请求数据，因此，完全可以认为是同一种业务，由同一个业务类来处理。而类似这样的业务类一般叫XXXUseCase或者XXXInteractor，当然啦，要是你的老大不打你，你喜欢叫这个类阿猫阿狗都随你。![](http://cdn.aixifan.com/dotnet/20130418/umeditor/dialogs/emotion/images/ac/01.gif)

  说了那么多，还是show me the demo直接点吧（完整的代码在这里：）。

  我的demo写得非常简（z）单（z），就是加载一张图片以及修改textView的文本，但为了展示上面的观点，我还是先将这两个需求分开来写，分别是GetPosterImageInteractor和GetPosterTitleInteractor:

  *<b>Interactor</b>*

    public interface Interactor<T> {
    void execute(T task);
    }
  *<b>GetPosterImageInteractor</b>*

    public class GetPosterImageInteractor extends AbstractInteractor<String>{

    Repository<String> mRepository;

    public GetPosterImageInteractor(ThreadExecutor threadExecutor, PostExecutor postExecutor, Repository<String> repository) {
        super(threadExecutor, postExecutor);
        this.mRepository = repository;
    }

    @Override
    public Observable<String> buildObservable() {
        return mRepository.getObservable();
      }
    }

  *<b>GetPosterImageInteractor</b>*

    public class GetPosterTitleInteractor extends AbstractInteractor<String>{

    Repository<String> mRepository;

    public GetPosterTitleInteractor(ThreadExecutor threadExecutor, PostExecutor postExecutor, Repository<String> repository) {
        super(threadExecutor, postExecutor);
        this.mRepository = repository;
    }

    @Override
    public Observable<String> buildObservable() {
        return mRepository.getObservable();
      }
    }

  由于篇幅起见，我就不附上基类的代码了<del>反正也没人看</del>，完整的代码我已经放在github上了。从上面的代码可以看出，domain层其实是将项目的业务抽取出来，比如，获取数据、加密、上传、计算等等，这些业务都交由一个个interactor来处理，presenter层需要哪种业务，就调用哪个interactor的execute()方法即可。同时，由于这些业务大多是耗时的操作，因此，interactor这一层还包含有线程的概念。

  还不理解的童鞋，就想想我举的绘制海报的例子，domain层对应a，a的职责是构思海报，注意构思的是海报的内容，而不是构思海报的布局，布局是c的事，而内容就相当于是项目的业务，需要什么业务，就在domain层抽取出来。嗯，大概就是这样吧。![](http://cdn.aixifan.com/dotnet/20130418/umeditor/dialogs/emotion/images/ac2/41.gif)

  理解之后，我们可以更进一步，就像我们上面说得，一种动作对应一个业务类，而获取图片和获取标题都可以认为是获取数据，因此，可以用同一个interactor来处理：

  *<b>GetDataInteractor</b>*

    public class GetDataInteractor<T> extends AbstractInteractor<T> {

    Repository<T> mRepository;

    public GetDataInteractor(ThreadExecutor threadExecutor, PostExecutor postExecutor,Repository<T> repository) {
        super(threadExecutor, postExecutor);
        this.mRepository = repository;
      }

    @Override
    public Observable<T> buildObservable() {
        return mRepository.getObservable();
      }
    }
  这样的话，就将代码简化了，presenter层需要获取什么样的数据，只要传入对应的泛型即可。当然啦，这种简化适用于业务相同的情况，如果请求数据的方式不同，还是需要分开写滴。


----------
**<h3>data层</h3>**
  data层是数据层，负责处理与数据相关的操作，例如，数据的获取，数据的转换，数据的保存啥的。有种说法是，这一层也应该是纯java的，但是我认为这不太可行，很简单，假如，这一层的javaBean需要实现parcelable接口呢？总不能因噎废食，就不实现这个接口吧，那这就实在是![](http://cdn.aixifan.com/dotnet/20130418/umeditor/dialogs/emotion/images/ac2/52.gif)

  data层相对于domain层来说会好理解的多，简单的说，就是与数据相关的操作都放在这里就对了。

  废话就不说了，还是直接上代码吧：

  *<b>GetPosterImageRepository</b>*

    public class GetPosterImageRepository implements Repository<String> {
    @Override
    public Observable<String> getObservable() {
        return Observable.just("http://cdn.aixifan.com/dotnet/20130418/umeditor/dialogs/emotion/images/ac2/29.gif");
      }
    }
  *<b>GetPosterTitleRepository</b>*

    public class GetPosterTitleRepository implements Repository<String>{
    @Override
    public Observable<String> getObservable() {
        return Observable.just("NicoNicoNi~");
      }
    }

  为了简便<del>懒</del>，我在demo里没有向后台请求数据，而是直接模拟了。可能有些童鞋不清楚observable是什么东东，这个其实是吊炸天的rxJava，因为我觉得听说过干净架构的人一般都会知道rxJava这个框架，所以我就不废话了，不清楚的童鞋可以看我后面的参考链接~

  data层就像是一个黑盒子，通过repository与外面连接，外面不需要知道repository提供的数据是从云端、内存还是磁盘上获得的，只要repository能提供正确的数据就可以了。因此，如果后期迭代的时候，data获取数据的逻辑修改了，并不影响其他层的代码，因为他们是通过接口关联在一起的，这也体现了干净架构传递出来的低耦合的设计理念。关于这一块的代码，可以参照Android-CleanArchitecture这个项目的。

  作为灵魂画手，我的绘画力并没有达到洪荒的境界，因此，这一层我就不灵魂作画，而是奉上一张别人绘制的图解给大家理解下data层的设计作为这一节的结尾吧：

![](https://pic3.zhimg.com/bb5dff59a0db45e886df41279f80a24e_b.jpg)

----------
**<h3>presenter层</h3>**
  presenter层是表现层，往简单说的话，这一层其实就是与UI相关的操作，往复杂说的话，这一层可以使用MVC、MVP甚至MVVM等模式，这几个模式都是可以单独拿出来讲的东东，但是对于各位大神来说，这几个模式怎么可能难得到你们，所以我也就不累述了~

  翠花，上<del>酸菜</del>代码：

  *<b>PosterContract</b>*

    public interface PosterContract {

    interface View extends IView {

        void setPosterImage(String imageUrl);

        void setPosterTitle(String title);
      }

    interface Presenter extends IPresenter{

        void getPosterImage();

        void getPosterTitle();

      }

    }

  *<b>PosterPresenter</b>*

    public class PosterPresenter implements PosterContract.Presenter {

    GetPosterImageInteractor mImageInteractor;
    GetPosterTitleInteractor mTitleInteractor;
    PosterContract.View mView;

    public PosterPresenter(PosterContract.View view){
        this.mView = view;
        mImageInteractor = new GetPosterImageInteractor(new JobExecutor(),new UIExecutor(),new GetPosterImageRepository());
        mTitleInteractor = new GetPosterTitleInteractor(new JobExecutor(),new UIExecutor(),new GetPosterTitleRepository());
    }

    @Override
    public void getPosterImage() {
        mImageInteractor.execute(new DefaultSubscriber<String>(){
            @Override
            public void onNext(String s) {
                mView.setPosterImage(s);
            }
        });
      }

    @Override
    public void getPosterTitle() {
        mTitleInteractor.execute(new DefaultSubscriber<String>(){
            @Override
            public void onNext(String s) {
                mView.setPosterTitle(s);
            }
        });
      }
    }
 
  PosterActivity的代码我就不贴出来了，清楚MVP模式的童鞋都知道，套路来的，<del>并不需要真心</del>，而对于MVP模式不清楚的童鞋可以看我后面的参考链接。从上面的代码也可以看出，presenter依赖于domain层，但并不依赖于data层，而因为domain层又依赖于data层，因此形成一个单向的链条，即：data -- > domain -- > presenter。

----------
**<h3>总结</h3>**

  经过上面的阐述，大家应该对干净架构有了一定的认识了吧，那么应该会有童鞋有疑问，搞得那么复杂，到底有什么<del>卵用</del>好处呢？其实，好处是显而易见的：

- 易维护
- 易测试
- 高内聚
- 低耦合
  

  易维护、高内聚以及低耦合比较容易理解，至于易测试这一点的话，由于各个模块是彼此独立的，因此，可以单独写单元测试来对各个模块进行相应的测试。关于测试这点，我打算在写Mockito的时候再详细写写。总而言之，就我的感觉来说，起码写起代码的时候条理会清晰很多，该写什么，该在哪里写，哪里出问题了也比较容易追踪。

  既然好处那么多，<del>那么狗蛋</del>，代价呢？代价不是也是显而易见的吗？简简单单一个项目，竟然写了那么多代码，累不累啊![](http://cdn.aixifan.com/dotnet/20130418/umeditor/dialogs/emotion/images/ac/01.gif)

  咳咳，干净架构这玩意我在网上找了不少资料，大抵说的模模糊糊，并不能让人醍醐灌顶，因此，在github上看了好几个开源项目，并在亲身尝试之后总结出这篇<del>什么玩意</del>博客，由于水平有限，表述粗糙，欢迎讨论、拍砖<del>，谢绝拍脸</del>。


----------
**<h3>参考资料</h3>**

- The Clean Architecture ： [https://8thlight.com/blog/uncle-bob/2012/08/13/the-clean-architecture.html](https://8thlight.com/blog/uncle-bob/2012/08/13/the-clean-architecture.html)
- 一种更清晰的Android架构 ：[https://zhuanlan.zhihu.com/p/20001838](https://zhuanlan.zhihu.com/p/20001838)
- Android-CleanArchitecture ：[https://github.com/android10/Android-CleanArchitecture](https://github.com/android10/Android-CleanArchitecture)
- 给 Android 开发者的 RxJava 详解 ：[http://gank.io/post/560e15be2dca930e00da1083](http://gank.io/post/560e15be2dca930e00da1083)
- 浅谈 MVP in Android  ：[http://blog.csdn.net/lmj623565791/article/details/46596109](http://blog.csdn.net/lmj623565791/article/details/46596109)
