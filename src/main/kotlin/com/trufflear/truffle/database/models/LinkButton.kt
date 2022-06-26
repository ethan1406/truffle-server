package com.trufflear.truffle.database.models

import org.jetbrains.exposed.dao.id.IntIdTable

object LinkButtons: IntIdTable("LINK_BUTTONS") {
    val imageUrl = varchar("image_url", 2083)
    val text = varchar("text", 50)
    val colorCode = varchar("color_code", 10)
    val transformationId = reference("transformation_id", CardTransformations)
}