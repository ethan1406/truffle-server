package com.trufflear.truffle

import com.trufflear.truffle.database.models.AugmentedImages
import com.trufflear.truffle.database.models.CardTransformations
import com.trufflear.truffle.database.models.LinkButtons
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.leftJoin
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import javax.sql.DataSource

internal class CardTransformationService(
        private val dataSource: DataSource
): CardTransformationGrpcKt.CardTransformationCoroutineImplBase() {

    override suspend fun getCardTransformationData(request: GetCardTransformationDataRequest): GetCardTransformationDataResponse {

        val augmentedTransformationMap = mutableMapOf<EntityID<Int>, AugmentedTransformation.Builder>()
        val attachmentViewMap = mutableMapOf<EntityID<Int>, AttachmentView.Builder>()
        val augmentedImageMap = mutableMapOf<EntityID<Int>, MutableList<AugmentedImage>>()
        val linkButtonMap= mutableMapOf<EntityID<Int>, MutableList<LinkButton>>()

        transaction (Database.connect(dataSource)){
            populateTransformationData(augmentedTransformationMap, attachmentViewMap)
            populateLinkButtonData(linkButtonMap)
            populateAugmentedImageData(augmentedImageMap)
        }

        linkButtonMap.forEach { (key, value) ->
            attachmentViewMap[key]?.addAllLinkButtons(value)
        }

        attachmentViewMap.forEach { (key, value) ->
            augmentedTransformationMap[key]?.attachmentView = value.build()

        }

        augmentedImageMap.forEach { (key, value) ->
            augmentedTransformationMap[key]?.addAllAugmentedImages(value)
        }

        return GetCardTransformationDataResponse.newBuilder()
                .addAllAugmentedTransformations(augmentedTransformationMap.values.map { it.build() })
                .build()
    }

    private fun populateAugmentedImageData(imageMap: MutableMap<EntityID<Int>, MutableList<AugmentedImage>>) {
        AugmentedImages.leftJoin(CardTransformations, { transformationId }, { id })
                .selectAll()
                .forEach {
                    val transformationId = it[CardTransformations.id]
                    if (imageMap.contains(transformationId).not()) {
                        imageMap[transformationId] = mutableListOf()
                    }

                    imageMap[transformationId]?.add(
                            augmentedImage {
                                imageUrl = it[AugmentedImages.imageUrl]
                                imageName = it[AugmentedImages.name]
                                physicalImageSize = size {
                                    width = it[AugmentedImages.physicalWidth]
                                    height = it[AugmentedImages.physicalHeight]
                                }
                            }
                    )
                }
    }


    private fun populateLinkButtonData(buttonMap: MutableMap<EntityID<Int>, MutableList<LinkButton>>) {
        LinkButtons.leftJoin(CardTransformations, { transformationId }, { id })
                .selectAll()
                .forEach {
                    val transformationId = it[CardTransformations.id]
                    if (buttonMap.contains(transformationId).not()) {
                        buttonMap[transformationId] = mutableListOf()
                    }

                    buttonMap[transformationId]?.add(
                            linkButton {
                                imageUrl = it[LinkButtons.imageUrl]
                                colorCode = it[LinkButtons.colorCode]
                                text = it[LinkButtons.text]
                            }
                    )
                }
    }

    private fun populateTransformationData(
            transformationMap: MutableMap<EntityID<Int>, AugmentedTransformation.Builder>,
            AttachmentViewMap: MutableMap<EntityID<Int>, AttachmentView.Builder>

    ) {
        CardTransformations.selectAll()
                .forEach {
                    val transformationId = it[CardTransformations.id]
                    if (transformationMap.contains(transformationId).not() &&
                            AttachmentViewMap.contains(transformationId).not()) {
                        transformationMap[transformationId] = AugmentedTransformation.newBuilder()
                                .setAnimationEffect(
                                        animationEffect {
                                            lottieUrl = it[CardTransformations.effectLottieUrl]
                                            minScale = it[CardTransformations.effectMinScale]
                                            maxScale = it[CardTransformations.effectMaxScale]
                                            effectViewSize = size {
                                                width = it[CardTransformations.effectUiViewWidth]
                                                height = it[CardTransformations.effectUiViewHeight]
                                            }
                                            position = position {
                                                xScaleToImageWidth = it[CardTransformations.effectPositionXScaleToImageWidth]
                                                zScaleToImageHeight = it[CardTransformations.effectPositionZScaleToImageHeight]
                                                y = it[CardTransformations.effectPositionY]
                                            }
                                        }
                                )

                        AttachmentViewMap[transformationId] = AttachmentView.newBuilder()
                                .setAttachmentWidthScaleToImageWidth(it[CardTransformations.attachmentWidthScaleToImageWidth])
                                .setMinScale(it[CardTransformations.attachmentMinScale])
                                .setMaxScale(it[CardTransformations.attachmentMaxScale])
                                .setAttachmentUiViewSize(
                                        size {
                                            width = it[CardTransformations.attachmentUiViewWidth]
                                            height = it[CardTransformations.attachmentUiViewHeight]
                                        }
                                )
                                .setPosition(
                                        position {
                                            xScaleToImageWidth = it[CardTransformations.attachmentPositionXScaleToImageWidth]
                                            zScaleToImageHeight = it[CardTransformations.attachmentPositionZScaleToImageHeight]
                                            y = it[CardTransformations.attachmentPositionY]
                                        }
                                )
                    }
                }
    }
}