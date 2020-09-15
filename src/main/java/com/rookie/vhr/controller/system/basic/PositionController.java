package com.rookie.vhr.controller.system.basic;

import com.rookie.vhr.model.Position;
import com.rookie.vhr.model.RespBean;
import com.rookie.vhr.service.PositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author ro0ki4
 * @data 2020/9/15 8:37
 * version 1.0
 */
@RestController
@RequestMapping("/system/basic/pos")
public class PositionController {

    @Autowired
    PositionService positionService;

    @GetMapping("/")
    public List<Position> getAllPosition(){
        return positionService.getAllPosition();
    }
    @PostMapping("/")
    public RespBean addPosition(@RequestBody Position position){
        if( positionService.addPosition(position) == 1){
            return RespBean.ok("添加成功");
        }
        return RespBean.error("添加失败");
    }

    @PutMapping("/")
    public RespBean updatePosition(@RequestBody Position position){
        if(positionService.updatePosition(position) == 1){
            return RespBean.ok("更新成功");
        }
        return RespBean.error("更新失败");

    }

    @DeleteMapping("/{id}")
    public RespBean deletePositionById(@PathVariable Integer id){
        if(positionService.deletePositionById(id) == 1){
            return RespBean.ok("删除成功");
        }else
            return RespBean.error("删除失败");
    }
}
