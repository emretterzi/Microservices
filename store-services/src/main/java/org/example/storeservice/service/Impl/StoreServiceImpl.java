package org.example.storeservice.service.Impl;

import lombok.RequiredArgsConstructor;
import org.example.storeservice.dto.response.StoreResponse;
import org.example.storeservice.repository.StoreRepository;
import org.example.storeservice.service.StoreService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StoreServiceImpl implements StoreService {

    private final StoreRepository storeRepository;

    @Override
    @Transactional(readOnly = true) //okuma yapar, herhangi bir şekilde veritabanı değişikliklerine neden olmaz
    public List<StoreResponse> isInStock(List<String> skuCode) {
        return storeRepository.findBySkuCodeIn(skuCode) //sipariş oluşturulmak istenen ürünlerin skuCode'ları alınır
                .stream()
                .map(store -> StoreResponse.builder() // Her bir ürün için yeni bir StoreResponse objesi oluşturur.
                        .skuCode(store.getSkuCode()) // StoreResponse'a ürünün sku kodunu ekle.
                        .isInStock(store.getQuantity() > 0) //stok 0'dan fazlaysa true, az ise false kaydet
                        .build() // StoreResponse oluşturur.
                ).toList();
    }

}
