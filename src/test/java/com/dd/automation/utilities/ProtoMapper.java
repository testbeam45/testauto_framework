package com.dd.automation.utilities;

import static com.google.common.collect.Lists.newArrayList;
import static org.apache.commons.lang3.StringUtils.EMPTY;

import com.google.protobuf.Descriptors;
import com.google.protobuf.Message;
import java.util.List;
import java.util.Map;

public class ProtoMapper {

    public static <T> List<T> transposeAndCast(List<Map<String,String>> table, Message prototype) {
        Message message = prototype.getDefaultInstanceForType();
        List<T> protolist = newArrayList();

        table.forEach(item -> {
            Message.Builder builder = message.toBuilder();
            item.entrySet().forEach(entry -> {
                Descriptors.FieldDescriptor temp = message.getDescriptorForType().findFieldByName(entry.getKey());
                if (temp != null) {
                    if (temp.getType().equals(Descriptors.FieldDescriptor.Type.BOOL)) {
                        if (!entry.getValue().equals(EMPTY))
                            builder.setField(temp, Boolean.valueOf(entry.getValue()));
                    } else builder.setField(temp, entry.getValue());
                }
            });
            protolist.add((T) builder.build());
        });
        return protolist;
    }

    public static Message.Builder transposeAndCast(Map<String,String> table, Message prototype) {
        Message message = prototype.getDefaultInstanceForType();
        Message.Builder builder = message.toBuilder();
        table.entrySet().forEach(item -> {


                Descriptors.FieldDescriptor temp = message.getDescriptorForType().findFieldByName(item.getKey());
                if (temp != null) {
                    if (temp.getType().equals(Descriptors.FieldDescriptor.Type.BOOL)) {
                        if (!item.getValue().equals(EMPTY))
                            builder.setField(temp, Boolean.valueOf(item.getValue()));
                    } else builder.setField(temp, item.getValue());
                }
             builder.build();
        });
        return  builder;
    }

}
