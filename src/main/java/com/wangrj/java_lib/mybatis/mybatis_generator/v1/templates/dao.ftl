package ${daoPackage};

import com.wangrj.java_lib.mybatis.MybatisDao;
import ${pojoName};

<#if createAnno>
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.stereotype.Repository;

@MapperScan
@Repository
</#if>
public interface ${daoSimpleName} extends MybatisDao<${pojoSimpleName}> {

}
