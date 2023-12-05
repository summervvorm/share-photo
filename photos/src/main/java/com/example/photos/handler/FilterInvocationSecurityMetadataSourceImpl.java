package com.example.photos.handler;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.example.photos.mapper.RoleMapper;
import com.example.photos.model.dto.ResourceRoleDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.util.AntPathMatcher;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;

/**
 * @Auther: Arrow
 * @Date: 2023/5/8
 * @Description: com.example.photos.handler
 */
public class FilterInvocationSecurityMetadataSourceImpl implements FilterInvocationSecurityMetadataSource {

    @Resource
    private RoleMapper roleMapper;

    private static List<ResourceRoleDTO> resourceRoleList;

    @PostConstruct
    private void loadResourceRoleList() {
        resourceRoleList = roleMapper.listResourceRoles();
    }

    public void clearDataSource() {
        resourceRoleList = null;
    }

    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
        if (CollectionUtils.isEmpty(resourceRoleList)) {
            this.loadResourceRoleList();
        }
        FilterInvocation fi = (FilterInvocation) object;
        String method = fi.getRequest().getMethod();
        String url = fi.getRequest().getRequestURI();
        AntPathMatcher antPathMatcher = new AntPathMatcher();
        for (ResourceRoleDTO resourceRoleDTO : resourceRoleList) {
            if (antPathMatcher.match(resourceRoleDTO.getUrl(), url) && resourceRoleDTO.getRequestMethod().equals(method)) {
                List<String> roleList = resourceRoleDTO.getRoleList();
                if (CollectionUtils.isEmpty(roleList)) {
                    return SecurityConfig.createList("disable");
                }
                return SecurityConfig.createList(roleList.toArray(new String[]{}));
            }
        }
        return null;
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return FilterInvocation.class.isAssignableFrom(clazz);
    }
}
