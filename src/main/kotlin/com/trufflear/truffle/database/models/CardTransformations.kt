package com.trufflear.truffle.database.models

import org.jetbrains.exposed.dao.id.IntIdTable

object CardTransformations: IntIdTable("CARD_TRANSFORMATIONS") {
    // video columns
    val videoUrl = varchar("image_url", 2083)
    val videoDimensionWidthPx = integer("video_dimension_width_px")
    val videoDimensionHeightPx = integer("video_dimension_height_px")
    val arVideoWidthScaleToImageWidth = float("ar_video_width_scale_to_image_width")

    val videoPositionXScaleToImageWidth = float("video_position_x_scale_to_image_width")
    val videoPositionZScaleToImageHeight = float("video_position_z_scale_to_image_height")
    val videoPositionY = float("video_position_y")

    // attachment columns
    val attachmentUiViewWidth = float("attachment_ui_view_width")
    val attachmentUiViewHeight = float("attachment_ui_view_height")
    val attachmentWidthScaleToImageWidth = float("attachment_width_scale_to_image_width")
    val attachmentMinScale = float("attachment_min_scale")
    val attachmentMaxScale = float("attachment_max_scale")
    val attachmentPositionXScaleToImageWidth = float("attachment_position_x_scale_to_image_width")
    val attachmentPositionZScaleToImageHeight = float("attachment_position_z_scale_to_image_height")
    val attachmentPositionY = float("attachment_position_y")

    // effect columns
    val effectLottieUrl = varchar("effect_lottie_url", 2083)
    val effectUiViewWidth = float("effect_ui_view_width")
    val effectUiViewHeight = float("effect_ui_view_height")
    val effectMinScale = float("effect_min_scale")
    val effectMaxScale = float("effect_max_scale")
    val effectPositionXScaleToImageWidth = float("effect_position_x_scale_to_image_width")
    val effectPositionZScaleToImageHeight = float("effect_position_z_scale_to_image_height")
    val effectPositionY = float("effect_position_y")
}