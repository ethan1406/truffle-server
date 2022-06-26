package com.trufflear.truffle.database.models

import org.jetbrains.exposed.dao.id.IntIdTable

object AugmentedImages: IntIdTable("AUGMENTED_IMAGES") {
    val name = varchar("name", 50)
    val imageUrl = varchar("image_url", 2083)
    val physicalWidth = float("physical_width")
    val physicalHeight = float("physical_height")
    val transformationId = reference("transformation_id", CardTransformations)
}