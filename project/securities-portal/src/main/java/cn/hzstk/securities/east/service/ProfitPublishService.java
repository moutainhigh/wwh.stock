package cn.hzstk.securities.east.service;

import cn.hzstk.securities.east.domain.ProfitPublish;
import cn.hzstk.securities.east.mapper.ProfitPublishMapper;
import net.ryian.commons.StringUtils;
import net.ryian.orm.service.BaseService;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
import java.util.Map;

/**
* @description:
* @author: autoCode
* @history:
*/
@Component
public class  ProfitPublishService extends BaseService<ProfitPublish,ProfitPublishMapper> {

    /**
    * 根据条件查询分页
    * @param paramMap
    * @return
    */
    public List<ProfitPublish> query(Map<String, String> paramMap) {
        Example example = new Example(ProfitPublish.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("valid","1");
        String dt = paramMap.get("dt");
        if (!StringUtils.isEmpty(dt)) {
            criteria.andEqualTo("dt", dt);
        }
        String stockCode = paramMap.get("stockCode");
        if (!StringUtils.isEmpty(stockCode)) {
            criteria.andLike("stockCode", "%" + stockCode+ "%");
        }
        example.setOrderByClause("reportDate desc");

        return mapper.selectByExample(example);
    }

    /**
    * 根据条件保存数据
    * @param o
    * @return
    */
    public Long saveOrUpdate(ProfitPublish o) {
        Example example = new Example(ProfitPublish.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("valid","1");
        criteria.andEqualTo("dt", o.getDt());
        criteria.andEqualTo("stockCode", o.getStockCode());

        List<ProfitPublish> list = mapper.selectByExample(example);
        if(list != null && list.size() > 0) {
            o.setId(list.get(0).getId());
            //return 1l;
        }

        return super.saveOrUpdate(o);
    }
}
