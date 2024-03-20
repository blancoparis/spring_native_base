package com.example.demo;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.annotation.ConfigurationClassUtils;

/**
 * Bean definitions for {@link BaseApplication}.
 */
@Generated
public class BaseApplication__BeanDefinitions {
  /**
   * Get the bean definition for 'baseApplication'.
   */
  public static BeanDefinition getBaseApplicationBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(BaseApplication.class);
    beanDefinition.setTargetType(BaseApplication.class);
    ConfigurationClassUtils.initializeConfigurationClass(BaseApplication.class);
    beanDefinition.setInstanceSupplier(BaseApplication$$SpringCGLIB$$0::new);
    return beanDefinition;
  }
}
