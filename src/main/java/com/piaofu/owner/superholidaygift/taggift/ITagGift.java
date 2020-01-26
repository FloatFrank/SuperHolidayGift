package com.piaofu.owner.superholidaygift.taggift;

import lk.vexview.tag.components.VexTag;
/**
 * 此接口用于之后VexGifImage的扩展，暂时未使用
 */
public interface ITagGift {
    void start();
    void down();
    void stop();
    void refresh();
    VexTag[] getNewVexTag();
    void delete();
}
