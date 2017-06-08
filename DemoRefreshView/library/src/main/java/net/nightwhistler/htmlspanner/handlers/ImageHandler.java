/*
 * Copyright (C) 2011 Alex Kuiper <http://www.nightwhistler.net>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.nightwhistler.htmlspanner.handlers;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.eileng.app.library.util.Base64;
import com.eileng.app.library.util.StringUtil;

import net.nightwhistler.htmlspanner.SpanStack;
import net.nightwhistler.htmlspanner.TagNodeHandler;

import org.htmlcleaner.TagNode;

/**
 * Handles image tags.
 * <p>
 * The default implementation tries to load images through a URL.openStream(),
 * override loadBitmap() to implement your own loading.
 *
 * @author Alex Kuiper
 */
public class ImageHandler extends TagNodeHandler {

    private Context context;

    public ImageHandler(Context context) {
        this.context = context;

    }

    @Override
    public void handleTagNode(TagNode node, SpannableStringBuilder builder, int start, int end, SpanStack stack) {

        String src = node.getAttributeByName("src");
        builder.append("\uFFFC");
        Bitmap bitmap = loadBitmap(src);
        if (bitmap != null) {
            Drawable drawable = new BitmapDrawable(context.getResources(),
                    bitmap);

            int w = bitmap.getWidth() - 1;
            int h = bitmap.getHeight() - 1;
            String width = node.getAttributeByName("width");
            String height = node.getAttributeByName("height");

            float scale = context.getResources().getDisplayMetrics().scaledDensity / 2;

            if(!StringUtil.isEmpty(width) && !StringUtil.isEmpty(height)){
                w = Integer.parseInt(width);
                h = Integer.parseInt(height);

                w =(int)(w * scale);
                h =(int)(h * scale);
            }

            drawable.setBounds(0, 0, w, h);

            ImageSpan span = new ImageSpan(drawable, src);

            stack.pushSpan(span, start, builder.length());
        }


    }

    /**
     * Loads a Bitmap from the given url.
     *
     * @param url
     * @return a Bitmap, or null if it could not be loaded.
     */
    protected Bitmap loadBitmap(String url) {
        if (TextUtils.isEmpty(url)) return null;
        Bitmap bmp = null;
        try {
            if (url.startsWith("http:") || url.startsWith("https:")) {
                bmp = Glide.with(context).load(url).asBitmap().into(Target.SIZE_ORIGINAL,
                        Target.SIZE_ORIGINAL).get();
            } else {
                bmp = stringToBitmap(url);
            }

        } catch (Exception io) {
            io.printStackTrace();
            Log.e("err", io.getMessage());
        }
        return bmp;
    }


    public Bitmap stringToBitmap(String string) {

        Bitmap bitmap = null;
        if (!StringUtil.isEmpty(string)) {
            try {
                byte[] bitmapArray = Base64.decode(string.split(",")[1]);
                bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return bitmap;
    }
}
