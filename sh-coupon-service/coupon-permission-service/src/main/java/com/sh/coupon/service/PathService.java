package com.sh.coupon.service;

import com.sh.coupon.dao.PathRepository;
import com.sh.coupon.entity.Path;
import com.sh.coupon.vo.CreatePathRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 路径相关的服务功能实现
 * @author admin
 */
@Slf4j
@Service
public class PathService {
    private final PathRepository pathRepository;

    public PathService(PathRepository pathRepository) {
        this.pathRepository = pathRepository;
    }

    /**
     * 添加新的path到数据表中
     * @Parms request {@link CreatePathRequest} 参数必传，方法中不校验是否存在，为默认存在项。
     * @return 数据表的主键
     */
    public List<Integer> createPath(CreatePathRequest request){
        List<CreatePathRequest.PathInfo> pathInfos = request.getPathInfos();
        List<CreatePathRequest.PathInfo> validRequests = new ArrayList<CreatePathRequest.PathInfo>();

        List<Path> currentPaths = pathRepository.findAllByServiceName(pathInfos.get(0).getServiceName());
        if(CollectionUtils.isNotEmpty(currentPaths)){
            //遍历查看是否有已经存在数据库中的，如果存在则不添加
            for (CreatePathRequest.PathInfo pathInfo : pathInfos) {
                boolean isValid = true;
                String opMode = pathInfo.getOpMode();
                String pathPattern = pathInfo.getPathPattern();
                for (Path currentPath : currentPaths) {
                    if (opMode.equals(currentPath.getOpMode()) && pathPattern.equals(currentPath.getPathPattern())) {
                        isValid = false;
                        break;
                    }
                }
                if (isValid){
                    validRequests.add(pathInfo);
                }
            }
        }else {
            validRequests.addAll(pathInfos);
        }
        List<Path> paths = new ArrayList<Path>();;
        validRequests.forEach(p -> {
            paths.add(new Path(p.getPathPattern(),p.getHttpMethod(),p.getPathName()
            ,p.getServiceName(),p.getOpMode()));
        });
        return pathRepository.saveAll(paths).stream().map(Path::getId).collect(Collectors.toList());
    }
}
