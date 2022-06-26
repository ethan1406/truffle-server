package com.trufflear.truffle.database.models.scripts

import com.trufflear.truffle.database.models.AugmentedImages
import com.trufflear.truffle.database.models.CardTransformations
import com.trufflear.truffle.database.models.LinkButtons
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

object CreateCardTransformationScript {

    fun createCardTransformation() {
        transaction () {
            addLogger(StdOutSqlLogger)
            SchemaUtils.create(AugmentedImages, CardTransformations, LinkButtons)

            val cardTransformationId = CardTransformations.insert {
                it[videoUrl] = "https://truffle.s3.us-west-1.amazonaws.com/staging/transformationVideos/wedding_card.mp4"
                it[videoDimensionWidthPx] = 720
                it[videoDimensionHeightPx] = 1280
                it[arVideoWidthScaleToImageWidth] = 1.2f
                it[videoPositionXScaleToImageWidth] = 0f
                it[videoPositionZScaleToImageHeight] = 0f
                it[videoPositionY] = 0f

                it[attachmentUiViewWidth] = 260f
                it[attachmentUiViewHeight] = 100f
                it[attachmentWidthScaleToImageWidth] = 1.04f
                it[attachmentPositionXScaleToImageWidth] = 0f
                it[attachmentPositionZScaleToImageHeight] = 0.8f
                it[attachmentPositionY] = 0f
                it[attachmentMinScale] = 0.10f
                it[attachmentMaxScale] = 0.15f

                it[effectLottieUrl] = "https://truffle.s3.us-west-1.amazonaws.com/staging/linkButtonIcons/lottieEffects/confetti-congratulation-sparkle.json"
                it[effectUiViewWidth] = 200f
                it[effectUiViewHeight] = 200f
                it[effectPositionXScaleToImageWidth] = 0f
                it[effectPositionZScaleToImageHeight] = -0.5f
                it[effectPositionY] = -0.005f
                it[effectMinScale] = 0.10f
                it[effectMaxScale] = 0.15f
            } get CardTransformations.id


            AugmentedImages.insert {
                it[name] = "Daniel and Emily's wedding card"
                it[imageUrl] = "https://truffle.s3.us-west-1.amazonaws.com/staging/augmentedImages/wedding_invitation_cover.jpg"
                it[physicalWidth] = 0.141f
                it[physicalHeight] = 0.1095f
                it[transformationId] = cardTransformationId
            }

            LinkButtons.insert {
                it[imageUrl] = "https://truffle.s3.us-west-1.amazonaws.com/staging/linkButtonIcons/ic_instagram.png"
                it[text] = "Wedding Pics"
                it[colorCode] = "#4db6ac"
                it[transformationId] = cardTransformationId
            }

            LinkButtons.insert {
                it[imageUrl] = "https://truffle.s3.us-west-1.amazonaws.com/staging/linkButtonIcons/ic_calendar.png"
                it[text] = "Schedule"
                it[colorCode] = "#4db6ac"
                it[transformationId] = cardTransformationId
            }
        }
    }
}