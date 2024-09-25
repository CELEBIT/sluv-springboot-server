package com.sluv.domain.closet.service;

import com.sluv.domain.closet.dto.ClosetCountDto;
import com.sluv.domain.closet.dto.ClosetDomainDto;
import com.sluv.domain.closet.entity.Closet;
import com.sluv.domain.closet.exception.ClosetNotFoundException;
import com.sluv.domain.closet.repository.ClosetRepository;
import com.sluv.domain.user.entity.User;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ClosetDomainService {

    private final ClosetRepository closetRepository;

    @Transactional(readOnly = true)
    public Long countByUserId(Long userId) {
        return closetRepository.countByUserId(userId);
    }

    @Transactional(readOnly = true)
    public List<ClosetCountDto> getUserClosetList(User user) {
        return closetRepository.getUserClosetList(user);
    }

    @Transactional(readOnly = true)
    public Boolean checkDuplicate(String name, Long closetId) {
        return closetRepository.checkDuplicate(name, closetId);
    }

    @Transactional
    public void saveBasicCloset(User user) {
        Closet basicCloset = Closet.createBasic(user);
        closetRepository.save(basicCloset);
    }

    @Transactional(readOnly = true)
    public Closet findById(Long closetId) {
        return closetRepository.findById(closetId).orElseThrow(ClosetNotFoundException::new);
    }

    @Transactional
    public void changeClosetData(Closet closet, ClosetDomainDto dto) {
        closet.changeCloset(dto);
        closetRepository.save(closet);
    }

    @Transactional
    public void deleteById(Long userId) {
        closetRepository.deleteById(userId);
    }

    @Transactional(readOnly = true)
    public List<Closet> findAllByUserId(Long userId) {
        return closetRepository.findAllByUserId(userId);
    }

    @Transactional(readOnly = true)
    public Page<Closet> getUserAllCloset(Long userId, Pageable pageable) {
        return closetRepository.getUserAllCloset(userId, pageable);
    }

    @Transactional(readOnly = true)
    public Page<Closet> getUserAllPublicCloset(Long userId, Pageable pageable) {
        return closetRepository.getUserAllPublicCloset(userId, pageable);
    }

    @Transactional
    public void saveCloset(Closet closet) {
        closetRepository.save(closet);
    }

    @Transactional(readOnly = true)
    public List<Closet> getRecentAddCloset(Long itemId) {
        return closetRepository.getRecentAddCloset(itemId);
    }
}
