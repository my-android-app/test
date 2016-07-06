package com.dixiang.framework.network2;

import java.io.File;
import java.util.List;
import java.util.Map;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.ImageView;

import com.dixiang.framework.network.IResult;
import com.google.gson.reflect.TypeToken;
import com.koushikdutta.async.future.Future;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.builder.Builders;
import com.koushikdutta.ion.builder.LoadBuilder;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;

/**
 * 网络访问
 */
public class NetOld {

    private static final String TAG = NetOld.class.getSimpleName();

    public static NetB with(Context context) {
        return new NetB(Ion.with(context));
    }

    public static NetB with(Fragment fragment) {
        return new NetB(Ion.with(fragment));
    }

    public static void displayImage(String url, ImageView imageView) {
        displayImage(url, imageView, null, null, null);
    }

    public static void displayImage(String url, ImageView imageView, int defaultImageRes) {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(defaultImageRes)
                .showImageForEmptyUri(defaultImageRes)
                .showImageOnFail(defaultImageRes)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
        displayImage(url, imageView, options);
    }

    public static void displayImageWithFadeAnim(String url, ImageView imageView) {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .displayer(new FadeInBitmapDisplayer(2000))
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
        displayImage(url, imageView, options);
    }

    public static void displayImage(String url, ImageView imageView, int defaultImageRes, ImageLoadingListener listener) {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(defaultImageRes)
                .showImageForEmptyUri(defaultImageRes)
                .showImageOnFail(defaultImageRes)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
        displayImage(url, imageView, options, listener);
    }


    public static void displayImage(String url, ImageView imageView, DisplayImageOptions options) {
        displayImage(url, imageView, options, null, null);
    }

    public static void displayImage(String url, ImageView imageView, DisplayImageOptions options, ImageLoadingListener listener) {
        displayImage(url, imageView, options, listener, null);
    }

    /**
     * 加载图片
     *
     * @param uri              图片地址
     * @param imageView        ImageView
     * @param options          展示图片的配置
     * @param listener         加载回调
     * @param progressListener 加载过程回调
     */
    public static void displayImage(String uri, ImageView imageView, DisplayImageOptions options,
                                    ImageLoadingListener listener, ImageLoadingProgressListener progressListener) {
        ImageLoader.getInstance().displayImage(uri, imageView, options, listener, progressListener);
    }

    public static class NetB {

        private LoadBuilder<Builders.Any.B> loadBuilder;

        private boolean isLog;
        private boolean isLogDetail;
        private int cacheSec;

        public NetB(LoadBuilder<Builders.Any.B> b) {
            this.isLog = false;
            this.loadBuilder = b;
        }

        /**
         * 开启简单的log，显示访问地址，参数等
         *
         * @return
         */
        public NetB log() {
            return log(false);
        }

        /**
         * 开启log
         *
         * @param detail 开启更详细的网络请求log
         * @return
         */
        public NetB log(boolean detail) {
            this.isLog = true;
            this.isLogDetail = detail;
            return this;
        }

        /**
         * 缓存数据，如果在规定时间内再次请求将不会访问服务端
         * Http header Cache-Control: max-age=[cacheSec]
         *
         * @param cacheSec
         * @return
         */
        public NetB cache(int cacheSec) {
            this.cacheSec = cacheSec;
            return this;
        }

        /**
         * 获取网络数据
         *
         * @param metadata Api的封装
         * @param callback 回调函数
         * @param <T>      实现IResult接口
         */
        public <T extends IResult> void fetch(IMetadata<T> metadata, NetUICallback<T> callback) {
            if (isLog) Log.d(TAG, "[Url] " + metadata.getUrl());
            Builders.Any.B b = this.loadBuilder.load(metadata.getUrl());
            if (cacheSec > 0) {
                b = b.setHeader("Cache-Control", "max-age=" + cacheSec);
            } else {
                b = b.noCache();
            }
            if (isLog && isLogDetail) {
                b = b.setLogging(TAG, Log.DEBUG);
            }

            Builders.Any.U u = null;
            Map<String, Object> m = metadata.getParameters();
            if (m != null) {
                for (Map.Entry<String, Object> entry : m.entrySet()) {
                    final String key = entry.getKey();
                    final String value = String.valueOf(entry.getValue());
                    if (isLog) Log.d(TAG, "[Param] " + key + ": " + value);
                    if (u != null) {
                        u = u.setBodyParameter(key, value);
                    } else {
                        u = b.setBodyParameter(key, value);
                    }
                }
            }

            Builders.Any.M am = null;
            Map<String, Object> mm = metadata.getMultiParameters();
            if (mm != null) {
                for (Map.Entry<String, Object> entry : mm.entrySet()) {
                    final String key = entry.getKey();
                    final Object value = entry.getValue();
                    if (isLog) Log.d(TAG, "[Param] " + key + ": " + value);
                    if (am != null) {
                        if (value instanceof File) {
                            am = am.setMultipartFile(key, (File) value);
                        } else if (value instanceof List) {
                            List<?> list = (List<?>) value;
                            for (Object obj : list) {
                                if (obj instanceof File) {
                                    am.setMultipartFile(key + "[]", (File) obj);
                                } else {
                                    am.setMultipartParameter(key + "[]", String.valueOf(obj));
                                }
                            }
                        } else {
                            am = am.setMultipartParameter(key, String.valueOf(value));
                        }
                    } else {
                        if (value instanceof File) {
                            am = b.setMultipartFile(key, (File) value);
                        } else if (value instanceof List) {
                            List<?> list = (List<?>) value;
                            for (Object obj : list) {
                                if (obj instanceof File) {
                                    am = b.setMultipartFile(key + "[]", (File) obj);
                                } else {
                                    am = b.setMultipartParameter(key + "[]", String.valueOf(obj));
                                }
                            }
                        } else {
                            am = b.setMultipartParameter(key, String.valueOf(value));
                        }
                    }
                }
            }
            Future<T> r;
            if (u != null) {
                r = u.as(metadata.getType());
            } else if (am != null) {
                r = am.as(metadata.getType());
            } else {
                r = b.as(metadata.getType());
            }
            r.setCallback(callback);
        }

        /**
         * 获取网络数据
         *
         * @param url        api地址
         * @param parameters api参数
         * @param typeToken  Json转换后的类型
         * @param callback   回调函数
         * @param <T>        实现IResult接口
         */
        public <T extends IResult> void fetch(final String url, final Map<String, Object> parameters,
                                              final TypeToken<T> typeToken, NetUICallback<T> callback) {
            fetch(new BaseMetadata<T>() {
                @Override
                public String getUrl() {
                    return url;
                }

                @Override
                public Map<String, Object> getParameters() {
                    return parameters;
                }

                @Override
                public Map<String, Object> getMultiParameters() {
                    return null;
                }

                @Override
                public TypeToken<T> getType() {
                    return typeToken;
                }
            }, callback);
        }

        public <T extends IResult> void fetchMulti(final String url, final Map<String, Object> multiParameters,
                                                   final TypeToken<T> typeToken, NetUICallback<T> callback) {
            fetch(new BaseMetadata<T>() {
                @Override
                public String getUrl() {
                    return url;
                }

                @Override
                public Map<String, Object> getParameters() {
                    return null;
                }

                @Override
                public Map<String, Object> getMultiParameters() {
                    return multiParameters;
                }

                @Override
                public TypeToken<T> getType() {
                    return typeToken;
                }
            }, callback);
        }



        /*public <T extends IResult> void uploadFile(Context context, String token, Uri uri, final TypeToken<T> typeToken, final INetCallback<T> callback) {
            PutExtra extra = new PutExtra();
            extra.params = new HashMap<String, String>();
            IO.putFile(context, token, IO.UNDEFINED_KEY, uri, extra, new JSONObjectRet() {
                @Override
                public void onSuccess(JSONObject obj) {
                    try {
                        Gson gson = new Gson();
                        T result = gson.fromJson(obj.toString(), typeToken.getType());
                        callback.onCompleted(null, result);
                    } catch (Exception e) {
                        callback.onCompleted(e, null);
                    }
                }

                @Override
                public void onFailure(QiniuException ex) {
                    callback.onCompleted(ex, null);
                }
            });
        }*/

    }

}
