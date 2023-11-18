package com.project.shopapp.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductImageDTO {
    @JsonProperty("product_id")
    private long productId;

    @Size(min = 5,max = 200,message = "Image's name")
    @JsonProperty("image_url")
    private String imageUrl;
}
