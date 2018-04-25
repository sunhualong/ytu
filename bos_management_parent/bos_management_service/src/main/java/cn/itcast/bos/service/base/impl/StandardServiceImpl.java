package cn.itcast.bos.service.base.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.base.StandardDao;
import cn.itcast.bos.domain.base.Standard;
import cn.itcast.bos.service.base.StandardService;
@Service
@Transactional
public class StandardServiceImpl implements StandardService {

	@Autowired
	private StandardDao standardDao;

	@Override
	public Page<Standard> findAll(Pageable pageable) {
		return standardDao.findAll(pageable);
	}

	@Override
	public List<Standard> findByName(String name) {
		return standardDao.findByName(name);
	}

	@Override
	public List<Standard> findByStandardName(String string) {
		return standardDao.findByStandardName(string);
	}

	@Override
	public void save(Standard standard) {
		standardDao.save(standard);
	}

	@Override
	public List<Standard> findAll() {
		return standardDao.findAll();
	}
}
