package cn.itcast.bos.service.base.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.base.SubAreaDao;
import cn.itcast.bos.domain.base.FixedArea;
import cn.itcast.bos.domain.base.SubArea;
import cn.itcast.bos.service.base.SubAreaService;
@Service
@Transactional
public class SubAreaServiceImpl implements SubAreaService {
	
	@Autowired
	private SubAreaDao subAreaDao;

	@Override
	public void save(SubArea model) {
		model.setId(UUID.randomUUID().toString());
		subAreaDao.save(model);
	}

	@Override
	public Page<SubArea> findAll(Pageable pageable) {
		return subAreaDao.findAll(pageable);
	}

	@Override
	public List<SubArea> findByFixedAreaId(String id) {
		FixedArea fa=new FixedArea();
		fa.setId(id);
		return subAreaDao.findByFixedArea(fa);
	}

	@Override
	public List<SubArea> findAll() {
		return subAreaDao.findAll();
	}

	@Override
	public List<SubArea> findAllById(String ids) {
		List<SubArea> list =new ArrayList<>();
		if(ids!=null&&StringUtils.isNotBlank(ids)){
			String[] split = ids.split(",");
			for (String id : split) {
				SubArea subArea = subAreaDao.findById(id);
				list.add(subArea);
			}
		}
		return list;
	}

	@Override
	public List<SubArea> findGroupedSubArea() {
		return subAreaDao.findGroupedSubArea();
	}

}
