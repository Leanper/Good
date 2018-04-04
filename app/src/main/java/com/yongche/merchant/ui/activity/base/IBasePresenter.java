package com.yongche.merchant.ui.activity.base;

/**
 * MVPPlugin
 *  邮箱 784787081@qq.com
 */

public interface IBasePresenter<V extends IBaseView>{
    void attachView(V view);

    void detachView();
}
