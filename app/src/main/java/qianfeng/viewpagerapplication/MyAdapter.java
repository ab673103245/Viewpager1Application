package qianfeng.viewpagerapplication;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

/**
 * Created by Administrator on 2016/9/13 0013.
 */
public class MyAdapter extends PagerAdapter {// 这个PgerAdapter适配器，里面可以是任何数据源，可以是像微信一样的下面有4个框的滑动页面
    private List<ImageView> list;


    public MyAdapter(List<ImageView> list) {
        this.list = list;
    }

    @Override
    public int getCount() { // 这边是显示Viewpager可以滑动的次数
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) { // instantiateItem中返回的Object会就是这个方法的Object

        return view == object;
    }

    // destroyItem和instantiateItem,都要把原本的内容全部删除，再写，否则会报异常。
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(list.get(position % 5));  // 删除掉该删除的项，要跟instantiateItem保持一致。
        // 注意要把原来写的那个删除。
        // 或者像下面这样写，object实际上就是你要移除页面的View
//        container.removeView((View)object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        ImageView iv = list.get(position % 5); // 这个是会报异常的，如果getCount方法里面的值大于list的size，那么怎么办呢？
                                        // 这是集合里面的方法，没人能改，只能够从索引的值下手，%5，这样到第六张图片的时候，自动去list集合里面取第0个索引所对应的图片
        container.addView(iv);

//        container.addView(list.get(position));
        // addView(View v, int position);
        return iv;  // 记得这里要返回iv，         给isViewFromObject方法传递Object
    }
}
