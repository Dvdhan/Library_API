package Solo_Project.Library_API.domain.Page;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MultiResponse <T, P>{
    T data;
    P pageinfo;
}
