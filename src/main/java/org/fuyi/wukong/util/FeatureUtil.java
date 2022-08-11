package org.fuyi.wukong.util;

import org.fuyi.wukong.core.entity.FeatureCarrier;
import org.fuyi.wukong.core.entity.FieldDefinition;
import org.fuyi.wukong.core.entity.LayerDefinition;
import org.gdal.ogr.Feature;

import java.util.Set;

/**
 * @author: <a href="mailto:thread.zhou@gmail.com">Fuyi</a>
 * @time: 10/8/2022 10:00 pm
 * @since: 1.0
 **/
public class FeatureUtil {

    public static void fillFields(Feature feature, Set<FieldDefinition> fieldDefinitions, FeatureCarrier carrier) {
        fieldDefinitions.forEach(fieldDefinition -> {
            if (fieldDefinition.getType() == 0) {
                feature.SetField(fieldDefinition.getName(), Integer.valueOf(carrier.getFields().get(fieldDefinition.getName()).toString()));
            } else if (fieldDefinition.getType() == 2) {
                feature.SetField(fieldDefinition.getName(), Double.valueOf(carrier.getFields().get(fieldDefinition.getName()).toString()));
            } else {
                feature.SetField(fieldDefinition.getName(), carrier.getFields().get(fieldDefinition.getName()).toString());
            }
        });
    }

    public static void fillFields(Feature feature, LayerDefinition definition, FeatureCarrier carrier) {
        definition.getFieldDefinitions().forEach(fieldDefinition -> {
            if (fieldDefinition.getType() == 0) {
                carrier.putField(fieldDefinition.getName(), feature.GetFieldAsInteger(fieldDefinition.getName()));
            } else if (fieldDefinition.getType() == 2) {
                carrier.putField(fieldDefinition.getName(), feature.GetFieldAsDouble(fieldDefinition.getName()));
            } else {
                carrier.putField(fieldDefinition.getName(), feature.GetFieldAsString(fieldDefinition.getName()));
            }
        });
    }
}
