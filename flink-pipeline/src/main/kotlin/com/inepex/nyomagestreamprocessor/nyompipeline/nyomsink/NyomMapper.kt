package com.inepex.nyomagestreamprocessor.nyompipeline.nyomsink

import com.inepex.nyomagestreamprocessor.api.generatednyom.NyomOuterClass
import com.inepex.nyomagestreamprocessor.schema.elastic.nyom.Nyom
import org.mapstruct.CollectionMappingStrategy
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Mappings
import org.mapstruct.factory.Mappers

@Mapper(collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED)
interface NyomMapper {

    companion object {
        val INSTANCE = Mappers.getMapper(NyomMapper::class.java)
    }

    @Mappings(
            Mapping(source = "mapping.objectId1", target = "mapping.objectId1", defaultValue = ""),
            Mapping(source = "mapping.objectId2", target = "mapping.objectId2", defaultValue = ""),
            Mapping(source = "mapping.objectId3", target = "mapping.objectId3", defaultValue = "")
    )
    fun toProto(nyom: Nyom): NyomOuterClass.Nyom
}
