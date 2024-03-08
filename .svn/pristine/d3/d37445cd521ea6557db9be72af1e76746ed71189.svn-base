package com.kelloggs.promotions.promotionservice.service.impl;


import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.kelloggs.promotions.lib.entity.Promotion;
import com.kelloggs.promotions.lib.entity.PromotionCluster;
import com.kelloggs.promotions.lib.exception.ApiException;
import com.kelloggs.promotions.lib.model.PromotionClusterDTO;
import com.kelloggs.promotions.lib.repository.PromotionClusterRepo;
import com.kelloggs.promotions.lib.repository.PromotionRepo;
import com.kelloggs.promotions.lib.repository.WinnerConfigRepo;
import com.kelloggs.promotions.promotionservice.service.PromotionClusterService;

@Service
public class PromotionClusterServiceImpl implements PromotionClusterService {

    private final PromotionClusterRepo promotionClusterRepo;
    
    private final PromotionRepo promotionRepo;
    
    private final WinnerConfigRepo winnerConfigRepo;
    
    @PersistenceContext
	private EntityManager entityManager;

    public PromotionClusterServiceImpl(PromotionClusterRepo promotionClusterRepo,PromotionRepo promotionRepo,WinnerConfigRepo winnerConfigRepo) {
        this.promotionClusterRepo = promotionClusterRepo;
        this.promotionRepo = promotionRepo;
        this.winnerConfigRepo = winnerConfigRepo;
    }

	@Override
	public PromotionClusterDTO addPromotionCluster(PromotionClusterDTO promotionCluster) {
		PromotionCluster pc=null;
		PromotionClusterDTO finalPcDTO=null;
		if (!promotionCluster.getClusterName().isEmpty()) { // It will throw error if cluster name is empty
			Optional<PromotionCluster> savedPromCluster=promotionClusterRepo.findByClusterName(promotionCluster.getClusterName());
			if(savedPromCluster.isPresent()) { // It will throw the error if cluster name is already present
				throw new ApiException(HttpStatus.BAD_REQUEST,
						400, String.format("Cluster name is already exists in database "+ promotionCluster.getClusterName()));
			}
			pc= promotionClusterRepo.save(getPromotioCluster(promotionCluster));
			finalPcDTO=getPromotioClusterDTO(pc,null);
			
		}else {
			 throw new ApiException(HttpStatus.BAD_REQUEST,
						400, String.format("Cluster Name Not Found"));
		}
		return finalPcDTO;
		}
	
	PromotionCluster getPromotioCluster(PromotionClusterDTO promotionCluster) {
		PromotionCluster pc=new PromotionCluster();
		pc.setClusterName(promotionCluster.getClusterName());
		return pc;
	}
	
	PromotionClusterDTO getPromotioClusterDTO(PromotionCluster promotionCluster,List<Promotion> promotionList) {
		PromotionClusterDTO pc=new PromotionClusterDTO();
		pc.setClusterId(promotionCluster.getClusterId());
		pc.setClusterName(promotionCluster.getClusterName());
		pc.setPromotions(promotionList);
		return pc;
	}
	

	@Override
	public PromotionClusterDTO deletePromotionCluster(Integer clusterId) {
		Optional<PromotionCluster> pc=promotionClusterRepo.findById(clusterId);
		if(pc.isPresent()) { // It will throw the error if cluster id is not present in DB.
			Optional<List<Promotion>> optionalPromotionList=promotionRepo.findByPromotionClusterId(clusterId);
			if(optionalPromotionList.isPresent()) { // It is used to check promotions are linked to cluster id
				for(Promotion p:optionalPromotionList.get()) {
					Query query = entityManager.createNativeQuery(
							"select config_id from winner_selection_config_reference where promotion_id="
									+ p.getId());
					List<Integer> resultList = (List<Integer>) query.getResultList();
					if (!resultList.isEmpty()) {
						for (Integer configId : resultList) {
							winnerConfigRepo.deleteById(configId);
						}
					}
					promotionRepo.deleteById(p.getId());
				}
			}
			promotionClusterRepo.deleteById(clusterId);
		}else {
			 throw new ApiException(HttpStatus.BAD_REQUEST,
						400, String.format("Cluster Not Found In Database Id : "+clusterId));
		}
		PromotionClusterDTO dto=getPromotioClusterDTO(pc.get(),null);
		return dto;
	}
}
