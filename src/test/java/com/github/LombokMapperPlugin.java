/** * $Id: LombokMapperPlugin.java,v 1.0 2019-04-23 22:06 chenmin Exp $ */package github;import lombok.Data;import org.mybatis.generator.api.IntrospectedColumn;import org.mybatis.generator.api.IntrospectedTable;import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;import org.mybatis.generator.api.dom.java.Method;import org.mybatis.generator.api.dom.java.TopLevelClass;import org.mybatis.generator.config.CommentGeneratorConfiguration;import org.mybatis.generator.config.Context;import org.mybatis.generator.internal.util.StringUtility;import tk.mybatis.mapper.generator.MapperCommentGenerator;import tk.mybatis.mapper.generator.MapperPlugin;import java.util.Properties;/** * @author 陈敏 * @version $Id: LombokMapperPlugin.java,v 1.1 2019-04-23 22:06 chenmin Exp $ * Created on 2019-04-23 22:06 * My blog： https://www.chenmin.info */@Datapublic class LombokMapperPlugin extends MapperPlugin {    private FullyQualifiedJavaType superClass;    private boolean caseSensitive = false;    //开始的分隔符，例如mysql为`，sqlserver为[    private String beginningDelimiter = "";    //结束的分隔符，例如mysql为`，sqlserver为]    private String endingDelimiter = "";    //数据库模式    private String schema;    //注释生成器    private CommentGeneratorConfiguration commentCfg;    //强制生成注解    private boolean forceAnnotation;    //父类    private String classPath;    @Override    public void setContext(Context context) {        super.setContext(context);        //设置默认的注释生成器        commentCfg = new CommentGeneratorConfiguration();        commentCfg.setConfigurationType(MapperCommentGenerator.class.getCanonicalName());        context.setCommentGeneratorConfiguration(commentCfg);        //支持oracle获取注释#114        context.getJdbcConnectionConfiguration().addProperty("remarksReporting", "true");    }    @Override    public void setProperties(Properties properties) {        super.setProperties(properties);        String classPath = properties.getProperty("classPath");        if (StringUtility.stringHasValue(classPath)) {            this.classPath = classPath;        }        String caseSensitive = this.properties.getProperty("caseSensitive");        if (StringUtility.stringHasValue(caseSensitive)) {            this.caseSensitive = caseSensitive.equalsIgnoreCase("TRUE");        }        String forceAnnotation = this.properties.getProperty("forceAnnotation");        if (StringUtility.stringHasValue(forceAnnotation)) {            commentCfg.addProperty("forceAnnotation", forceAnnotation);            this.forceAnnotation = forceAnnotation.equalsIgnoreCase("TRUE");        }        String beginningDelimiter = this.properties.getProperty("beginningDelimiter");        if (StringUtility.stringHasValue(beginningDelimiter)) {            this.beginningDelimiter = beginningDelimiter;        }        commentCfg.addProperty("beginningDelimiter", this.beginningDelimiter);        String endingDelimiter = this.properties.getProperty("endingDelimiter");        if (StringUtility.stringHasValue(endingDelimiter)) {            this.endingDelimiter = endingDelimiter;        }        commentCfg.addProperty("endingDelimiter", this.endingDelimiter);        String schema = this.properties.getProperty("schema");        if (StringUtility.stringHasValue(schema)) {            this.schema = schema;        }    }    /**     * 生成基础实体类     *     * @param topLevelClass     * @param introspectedTable     * @return     */    @Override    public boolean modelBaseRecordClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {        this.processEntityClass(topLevelClass, introspectedTable);        return true;    }    /**     * 生成实体类注解KEY对象     *     * @param topLevelClass     * @param introspectedTable     * @return     */    @Override    public boolean modelPrimaryKeyClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {        this.processEntityClass(topLevelClass, introspectedTable);        return true;    }    /**     * 生成带BLOB字段的对象     *     * @param topLevelClass     * @param introspectedTable     * @return     */    @Override    public boolean modelRecordWithBLOBsClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {        this.processEntityClass(topLevelClass, introspectedTable);        return true;    }    @Override    public boolean modelGetterMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedColumn introspectedColumn, IntrospectedTable introspectedTable, ModelClassType modelClassType) {        return false;    }    @Override    public boolean modelSetterMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedColumn introspectedColumn, IntrospectedTable introspectedTable, ModelClassType modelClassType) {        return false;    }    /**     * 处理实体类的包和@Table注解     *     * @param topLevelClass     * @param introspectedTable     */    private void processEntityClass(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {        //引入Lombok注解        topLevelClass.addImportedType("lombok.Data");        topLevelClass.addImportedType("lombok.experimental.Accessors");        //引入JPA注解        topLevelClass.addImportedType("javax.persistence.*");        topLevelClass.addImportedType("javax.persistence.*");        FullyQualifiedJavaType superClass = this.getSuperClass();        if (superClass != null) {            topLevelClass.addImportedType(superClass);            topLevelClass.addSuperInterface(superClass);        }        String tableName = introspectedTable.getFullyQualifiedTableNameAtRuntime();        //如果包含空格，或者需要分隔符，需要完善        if (StringUtility.stringContainsSpace(tableName)) {            tableName = context.getBeginningDelimiter()                    + tableName                    + context.getEndingDelimiter();        }        //是否忽略大小写，对于区分大小写的数据库，会有用        if (caseSensitive && !topLevelClass.getType().getShortName().equals(tableName)) {            topLevelClass.addAnnotation("@Table(name = \"" + getDelimiterName(tableName) + "\")");        } else if (!topLevelClass.getType().getShortName().equalsIgnoreCase(tableName)) {            topLevelClass.addAnnotation("@Table(name = \"" + getDelimiterName(tableName) + "\")");        } else if (StringUtility.stringHasValue(schema)                || StringUtility.stringHasValue(beginningDelimiter)                || StringUtility.stringHasValue(endingDelimiter)) {            topLevelClass.addAnnotation("@Table(name = \"" + getDelimiterName(tableName) + "\")");        } else if(forceAnnotation){            topLevelClass.addAnnotation("@Table(name = \"" + getDelimiterName(tableName) + "\")");        }        // 添加Lombok注解        topLevelClass.addAnnotation("@Data");        topLevelClass.addAnnotation("@Accessors(chain = true)");    }    public FullyQualifiedJavaType getSuperClass() {        if (classPath != "" && classPath != null) {            superClass = new FullyQualifiedJavaType(classPath);            return superClass;        }        return null;    }}