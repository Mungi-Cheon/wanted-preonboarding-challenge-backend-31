package com.wanted.ecommerce.category.dto.request;

import com.wanted.ecommerce.common.utils.SortUtils;
import jakarta.validation.constraints.Min;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PageableRequest {
    @Min(value = 1)
    private Integer page = 1;
    @Min(value = 10)
    private Integer perPage = 10;
    private String sort = "created_at:desc";

    public Map<String, String> getSort(){
        return SortUtils.createSortMap(sort);
    }
}
