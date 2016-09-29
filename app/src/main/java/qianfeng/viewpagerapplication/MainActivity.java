package qianfeng.viewpagerapplication;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private int[] imgs = new int[]{R.drawable.p001, R.drawable.p002, R.drawable.p003, R.drawable.p004, R.drawable.p005};
    private List<ImageView> images;
    private LinearLayout dotLayout;
    private ViewPager viewPager;

    private int prePager = 0;

    private TextView tv;
    private String[] str = new String[]{"000","111","222","333","444"};

    private boolean isRunning = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        dotLayout = ((LinearLayout) findViewById(R.id.dot_layout));
        tv = ((TextView) findViewById(R.id.tv));

        initInfo();

        MyAdapter myAdapter = new MyAdapter(images);
        viewPager.setAdapter(myAdapter);

        viewPager.setOffscreenPageLimit(1); // 设置预加载页数，最小值为1

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            // 页面滚动过程中回调该方法
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            // 页面滑动结束时回调该方法
            @Override
            public void onPageSelected(int position) {

                position = position % 5; // 这里也要防止list超出索引，所以要对5取余，让第六张图片自动加载第一张图片，索引为0的图片
//                viewPager.getChildAt(prePager).setEnabled(false);
//                viewPager.getChildAt(position).setEnabled(true);
                // 这里是dotLayout的getChild，不是viewPager的getChild，注意不要搞混了，圆点是dotLayout的子控件，viewPager的子控件只有ImageView

                // 然后在这里设置textView就可以了，因为这样一个控件的TextView就可以于这个圆点绑定在一起，这样又方便又省控件，节约资源，是最好的做法
                // 相当于每次加载都给一个TextView重新赋值而已，由头到尾都只有一个TextView
                tv.setText(str[position]);

                dotLayout.getChildAt(prePager).setEnabled(false);
                dotLayout.getChildAt(position).setEnabled(true);
                prePager = position;

            }

            // 页面状态发生改变时，回调该方法
            //state三种取值：
            //0 表示页面静止
            //1 表示手指在ViewPager上拖动
            //2 表示手指离开ViewPager，页面自由滑动
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

//         设置圆点一开始指定的位置，在Integer.MAX_VALUE的中间(MAX_VALUE/2)的话，这样加载就左右两边都可以滑动(MAX_VALUE/2)次
        viewPager.setCurrentItem(Integer.MAX_VALUE / 2 - 3);  // 指定当前viewPager中显示的项
        // 这样设置好当前的item是 索引为 Integer.MAX_VALUE / 2 - 3, 可以向左和向右循环这么多次！
        // 加载的图片是 list.get((Integer.MAX_VALUE / 2 - 3) % 5)取余的这个图片，循环的位置是在
        // 可以向左及向右滑动 Integer.MAX_VALUE / 2次，  循环次数是MyAdapter的getCount（）方法所返回的值决定的

        // 隔3秒钟自动加载效果
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 开启一个子线程，注意这个子线程是无法更新UI的
                Log.d("google-my:", "run:1111111111111111111 ---------------------------------------" + Thread.currentThread());
                while(isRunning) // 这个标记是，当界面关闭时，子线程就不要再跑了，因为这样会浪费资源，子线程跑的前提是，界面还在。一旦界面finish(),就不再执行这个while循环，
                {
                    SystemClock.sleep(3000); // 因为主线程不允许阻塞，而这个睡眠3秒会引起阻塞，所以这个睡眠要放在子线程中执行，所以要开子线程，但是子线程又怎么更新UI呢？所以要用runOnUiThread来更新UI
                    // 子线程要这样更新UI,调用runOnUiThread()
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // 在这里设置当前要显示的view
                            Log.d("google-my:", "run: 22222222222222++++++++++++++++++" + Thread.currentThread()); // 这个是在主线程中执行的，要想更新UI，要就在主线程中更新Ui，就像onPost方法是执行在主线程一样。
                            viewPager.setCurrentItem(viewPager.getCurrentItem() + 1); // 睡眠3秒后，加载当前项的下一项，一直循环下去，就是向右循环
                        }
                    });
                }
            }
        }).start(); // 记住这个start（）来开启线程啊！！！卧槽

    }

    private void initInfo() {
        images = new ArrayList<>();
        for (int i = 0; i < imgs.length; i++) {
            ImageView imageView = new ImageView(this);
            imageView.setImageResource(imgs[i]);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);// 按比例缩放至填满ImageView，这是按比例缩放填满控件
            images.add(imageView);

            // 动态画 5个view，再把view的背景设为 圆，在Drawable里面
            View view = new View(this);

            // new 一个布局参数对象！！！   这个TypeValue是将 px 单位转换为 dp
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics()),
                    (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics()));

            lp.leftMargin = 20; // 设置lp距离左边是20px 得到LinearLayou的布局参数就有这个好处，可以设置leftMargin，昨天的iv。getLayou是没有leftMargin的，不同的容器类也不同


            view.setLayoutParams(lp); // 给view设置上述的LinearLayout设置好的布局参数
            view.setEnabled(false); // 统一将这个状态先设定为false
            view.setBackgroundResource(R.drawable.dot_bg);
            dotLayout.addView(view);


        }

        dotLayout.getChildAt(0).setEnabled(true); // dotLayout.getChildAt(0) 所有的父容器都有这个方法，得到指定索引的子控件，容器类才有，容器类也是一个List集合，按xml上写的顺序来排列


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isRunning = false; // 防止子线程无限循环下去，给个标记让子线程的while循环结束
    }
}
