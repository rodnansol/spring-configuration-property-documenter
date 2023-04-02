package org.rodnansol.gradle.tasks;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.rodnansol.core.generator.writer.CombinedInput;

@Mapper
public interface AggregationInputMapper {

    @Mapping(target = "sectionName", source = "name")
    CombinedInput toCombinedInput(AggregationInput aggregationInput);

}
