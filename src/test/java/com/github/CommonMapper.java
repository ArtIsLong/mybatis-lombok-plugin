/** * $Id: CommonMapper.java,v 1.0 2019-04-23 10:19 chenmin Exp $ */package github;import tk.mybatis.mapper.common.Mapper;import tk.mybatis.mapper.common.MySqlMapper;/** * 通用Mapper统一入口 * @author chenmin * @Description * @version $Id: CommonMapper.java,v 1.1 2019-04-23 10:19 chenmin Exp $ * @Date Created on 2019-04-23 10:19 */public interface CommonMapper<T> extends Mapper<T>, MySqlMapper<T> {}