package com.campusmarket.campusmarketserver.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ProductCreateRequest {

    @NotBlank(message = "标题不能为空")
    @Size(max = 64, message = "标题最长 64 字")
    private String title;

    private String description;

    @NotNull(message = "价格不能为空")
    @DecimalMin(value = "0.01", message = "价格必须大于 0")
    private BigDecimal price;

    @DecimalMin(value = "0.01", message = "原价必须大于 0")
    private BigDecimal originalPrice;

    @NotNull(message = "请选择分类")
    private Long categoryId;

    @NotNull(message = "请选择成色")
    private Integer condition;

    private String location;

    // 图片 URL 列表（第3.5课接入 MinIO 后改为上传返回的 URL）
    private List<String> images;
}