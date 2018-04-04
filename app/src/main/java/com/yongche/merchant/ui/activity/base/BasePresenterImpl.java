package com.yongche.merchant.ui.activity.base;

/**
 * MVPPlugin
 *  邮箱 784787081@qq.com
 */

public class BasePresenterImpl<V extends IBaseView> implements IBasePresenter<V> {
    protected V mView;
    @Override
    public void attachView(V view) {
        mView=view;
    }

    @Override
    public void detachView() {
        mView=null;
    }
}
