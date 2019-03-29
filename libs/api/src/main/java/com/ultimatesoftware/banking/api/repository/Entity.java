package com.ultimatesoftware.banking.api.repository;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonIgnore;
import org.bson.types.ObjectId;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class Entity {
    @BsonId
    protected ObjectId id;

    @JsonProperty("id")
    @BsonIgnore
    public String getHexId() {
        if (id != null) {
            return id.toHexString();
        }
        return null;
    }
}
