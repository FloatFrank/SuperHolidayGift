package com.piaofu.owner.superholidaygift.taggift;

import com.piaofu.owner.superholidaygift.SuperHolidayGift;
import com.piaofu.owner.superholidaygift.gift.Gift;
import lk.vexview.tag.TagDirection;
import lk.vexview.tag.components.VexImageTag;
import lk.vexview.tag.components.VexTag;
import lk.vexview.tag.components.VexTextTag;

/**
 * 此类为Tag的动态更新处理对象，直属于TagGift的调度
 */
public class TagSimpleBuilder {
    private static TagDirection tagDirection = null;
    private String url;
    private int width;
    private int height;
    private float xs;
    private float ys;

    public TagSimpleBuilder(String url, int width, int height, float xs, float ys) {
        this.url = url;
        this.width = width;
        this.height = height;
        this.xs = xs;
        this.ys = ys;
    }

    public static void readTagDirection() {
        tagDirection = new TagDirection(
                (float) SuperHolidayGift.plugin.getConfig().getDouble("angel_x"),
                (float) SuperHolidayGift.plugin.getConfig().getDouble("angel_y"),
                (float) SuperHolidayGift.plugin.getConfig().getDouble("angel_z"),
                SuperHolidayGift.plugin.getConfig().getBoolean("for_player"),
                true
        );

    }

    public String getUrl() {
        return url;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public float getXs() {
        return xs;
    }

    public float getYs() {
        return ys;
    }

    //返回VexTag数组 其1是图片对象 其2是文字介绍对象
    public VexTag[] getTag(String id, TagNowLoc tagNowLoc, Gift gift) {
        //后参为偏移操作
        VexTag[] vexImageTag = new VexTag[2];
        vexImageTag[0] = new VexImageTag(id, tagNowLoc.getX() + TagGift.getXMove(), tagNowLoc.getY() + TagGift.getYMove(), tagNowLoc.getZ() + TagGift.getZMove(), url, width, height, xs, ys, tagDirection);
        vexImageTag[1] = new VexTextTag(id+"_t", tagNowLoc.getX() + TagGift.getXMoveT(), tagNowLoc.getY() + TagGift.getYMoveT() + TagGift.getTextHeight(),tagNowLoc.getZ() + TagGift.getZMoveT(), gift.getDisplay(), TagGift.isTextBack(), tagDirection);
        return vexImageTag;
    }
}
