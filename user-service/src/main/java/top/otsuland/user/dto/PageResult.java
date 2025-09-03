package top.otsuland.user.dto;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageResult<T> {
    private long current;
    private long size;
    private long total;
    private long pages;
    private List<T> records; // 数据列表

    public static <T, R> PageResult<R> of(Page<T> page, Function<T, R> converter) {
        List<R> convertedRecords = page.getRecords()
            .stream()
            .map(converter)
            .collect(Collectors.toList());

        return new PageResult<>(
            page.getCurrent(),
            page.getSize(),
            page.getTotal(),
            page.getPages(),
            convertedRecords
        );
    }

    public static <T> PageResult<T> of(Page<T> page) {
        return new PageResult<>(
            page.getCurrent(),
            page.getSize(),
            page.getTotal(),
            page.getPages(),
            page.getRecords()
        );
    }
}
