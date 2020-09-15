package com.rookie.vhr.service;

import com.rookie.vhr.mapper.PositionMapper;
import com.rookie.vhr.model.Position;
import com.rookie.vhr.model.RespBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author ro0ki4
 * @data 2020/9/15 8:39
 * version 1.0
 */
@Service
public class PositionService {

    @Autowired
    PositionMapper positionMapper;

    public List<Position> getAllPosition(){
        return positionMapper.getAllPosition();
    }

    public Integer addPosition(Position position) {
        position.setEnabled(true);
        position.setCreateDate(new Date());
        return  positionMapper.insertSelective(position);
    }

    public Integer updatePosition(Position position) {
        return positionMapper.updateByPrimaryKeySelective(position);
    }

    public Integer deletePositionById(Integer id) {
        return positionMapper.deleteByPrimaryKey(id);
    }
}
