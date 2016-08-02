package ${entity.javaPackage};

<#list entity.importProperties as property>
import ${property.jarName}; 
</#list>

/**
 * This Dynamic Decode Type Bean.
 * @author chengpei@teg.cn
 *
 */
public class ${entity.className}<#if entity.superclass?has_content> extends ${entity.superclass} </#if>
{
<#list entity.propertyLines as propertyLine>
	<#if propertyLine??>${propertyLine}</#if>
</#list>

<#if entity.constructors>
    public ${entity.className}() {
    
    }
</#if>

	@Override
	public boolean isPacked() {
		 return true;
	}
	
<#if entity.needByteOrder>	
	public ByteOrder byteOrder(){			
		return ByteOrder.LITTLE_ENDIAN;
	}
</#if>
	
}
