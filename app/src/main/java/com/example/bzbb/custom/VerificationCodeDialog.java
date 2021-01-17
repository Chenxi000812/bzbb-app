package com.example.bzbb.custom;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaderFactory;
import com.bumptech.glide.load.model.LazyHeaders;
import com.bumptech.glide.request.RequestOptions;
import com.example.bzbb.Model.Cookie;
import com.example.bzbb.R;
import com.example.bzbb.Utils.HttpUtils;

import java.io.InputStream;
import java.util.Map;

import okhttp3.Response;


public class VerificationCodeDialog extends Dialog {

    private VerificationCodeDialog(Context context, int themeResId) {
        super(context, themeResId);
    }



    public static class Builder {

        private View mLayout;

        private ImageView mIcon;
        private EditText mEditText;
        private Button mButton;

        private VerificationCodeDialog mDialog;

        public Builder(Context context) {
            mDialog = new VerificationCodeDialog(context, R.style.Theme_AppCompat_Dialog);
            LayoutInflater inflater =
                    (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //加载布局文件
            mLayout = inflater.inflate(R.layout.dialog_verificationcode, null, false);
            //添加布局文件到 Dialog
            mDialog.addContentView(mLayout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));

            mIcon = mLayout.findViewById(R.id.imageView9);
            mEditText = mLayout.findViewById(R.id.editText4);
            mButton = mLayout.findViewById(R.id.dialog_confirmBtn);
        }

        /**
         * 通过 ID 设置 Dialog 图标
         */
        public Builder setIcon(int resId) {
            mIcon.setImageResource(resId);
            return this;
        }

        /**
         * 用 Bitmap 作为 Dialog 图标
         */
        public Builder setIcon(Bitmap bitmap) {
            mIcon.setImageBitmap(bitmap);
            return this;
        }

        /**
         * 设置按钮文字和监听
         */
        public Builder setButton(@NonNull String text) {
            mButton.setText(text);
            return this;
        }

        private static RequestOptions options = new RequestOptions().skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE);
        private static String VerificationCodeURL = "/sms/imgCode";
        private static void updateImgView(final ImageView imageView){
            final Handler imgcodeHandler = new Handler(){
                @Override
                public void handleMessage(Message msg) {
                    Bitmap bitmap = (Bitmap) msg.obj;
                    imageView.setImageBitmap(bitmap);
                }
            };
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Response response = HttpUtils.sessionGet(VerificationCodeURL);
                    InputStream inputStream = response.body().byteStream();
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    Message message = new Message();
                    message.obj = bitmap;
                    imgcodeHandler.sendMessage(message);
                }
            }).start();
        }

        public VerificationCodeDialog create() {
            mIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateImgView(mIcon);

                }
            });
            mButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDialog.dismiss();
                    if (mafterBtnOnClick!=null){
                        mafterBtnOnClick.processing(mEditText.getText().toString());
                    }
                }
            });
            mDialog.setContentView(mLayout);
            mDialog.setCancelable(true);                //用户可以点击后退键关闭 Dialog
            mDialog.setOnShowListener(new OnShowListener() {
                @Override
                public void onShow(DialogInterface dialog) {
                    updateImgView(mIcon);

                }
            });
            return mDialog;
        }

        public interface afterBtnOnClick{
            void processing(String inputcode);
        }
        private afterBtnOnClick mafterBtnOnClick;

        public Builder setAfterBtnOnClick(afterBtnOnClick afterBtnOnClick){
            mafterBtnOnClick = afterBtnOnClick;
            return this;
        }
    }
}