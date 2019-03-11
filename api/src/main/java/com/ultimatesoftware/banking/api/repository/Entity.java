package com.ultimatesoftware.banking.api.repository;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.types.ObjectId;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class Entity {
    @BsonId
    private ObjectId id;

    @JsonProperty("id")
    public String getHexId() {
        if (id != null) {
            return id.toHexString();
        }
        return null;
    }
}
