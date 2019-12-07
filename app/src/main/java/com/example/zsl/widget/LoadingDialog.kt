package com.example.zsl.widget


import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.Interpolator
import android.view.animation.RotateAnimation
import android.widget.ImageView
import com.example.zsl.R
import kotlinx.android.synthetic.main.progressbar_common_loading.*


class LoadingDialog(context: Context) : Dialog(context) {

    private var loadingImg: ImageView? = null
    private var rotateAnimation: RotateAnimation? = null

    override fun onCreate(savedInstanceState: Bundle) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.progressbar_common_loading)
        setCanceledOnTouchOutside(false)
        window!!.setBackgroundDrawableResource(android.R.color.transparent)//组件背景透明
        window!!.setDimAmount(0f)//Dialog周边透明
        loadingImg = iv_loading as ImageView
        initRotate()
    }

    private fun initRotate() {
        rotateAnimation =
            RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)//顺时针
        rotateAnimation!!.duration = 1000//一秒
        val petalCount = 12//多少花瓣数量就是多少
        rotateAnimation!!.interpolator = Interpolator { input -> (input * petalCount).toInt().toFloat() / petalCount }
        rotateAnimation!!.repeatCount = -1//重复次数
    }

    override fun onStart() {
        super.onStart()
        loadingImg!!.startAnimation(rotateAnimation)
    }

    override fun onStop() {
        super.onStop()
        loadingImg!!.clearAnimation()
    }

    companion object {
        /**
         * 显示loading动画
         * @return dialog实例
         */
        fun show(context: Context, dialog: Dialog?): Dialog {
            var dialog = dialog

            if (dialog == null) {
                dialog = LoadingDialog(context)
            }
            if (!dialog.isShowing) {
                dialog.show()
            }
            return dialog
        }

        /**
         * 隐藏loading动画
         *
         * @param dialog
         */
        fun dismiss(dialog: Dialog?) {

            if (dialog != null && dialog.isShowing) {
                dialog.dismiss()
            }
        }

        /**
         * Dialog是否展示
         * @param dialog
         * @return
         */
        fun isShowing(dialog: Dialog?): Boolean {
            return dialog != null && dialog.isShowing
        }
    }
}
