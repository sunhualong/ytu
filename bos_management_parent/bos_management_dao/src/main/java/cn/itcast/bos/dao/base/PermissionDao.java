package cn.itcast.bos.dao.base;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import cn.itcast.bos.domain.system.Permission;

public interface PermissionDao extends JpaRepository<Permission, Integer> {

	@Query("select p from Permission p join p.roles r join r.users u where u.id=?")
	List<Permission> findByUser(Integer id);

}
