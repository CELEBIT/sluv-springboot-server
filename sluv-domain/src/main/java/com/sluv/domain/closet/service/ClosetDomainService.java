package com.sluv.domain.closet.service;

import com.sluv.domain.closet.dto.ClosetCountDto;
import com.sluv.domain.closet.dto.ClosetDomainDto;
import com.sluv.domain.closet.entity.Closet;
import com.sluv.domain.closet.exception.ClosetNotFoundException;
import com.sluv.domain.closet.repository.ClosetRepository;
import com.sluv.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClosetDomainService {

    private final ClosetRepository closetRepository;

    public Long countByUserId(Long userId) {
        return closetRepository.countByUserId(userId);
    }

    public List<ClosetCountDto> getUserClosetList(User user) {
        return closetRepository.getUserClosetList(user);
    }

    public Boolean checkDuplicate(String name, Long closetId) {
        return closetRepository.checkDuplicate(name, closetId);
    }

    public void saveBasicCloset(User user) {
        Closet basicCloset = Closet.createBasic(user);
        closetRepository.save(basicCloset);
    }

    public Closet findById(Long closetId) {
        return closetRepository.findById(closetId).orElseThrow(ClosetNotFoundException::new);
    }

    public void changeClosetData(Closet closet, ClosetDomainDto dto) {
        closet.changeCloset(dto);
        closetRepository.save(closet);
    }

    public void deleteById(Long userId) {
        closetRepository.deleteById(userId);
    }

    public List<Closet> findAllByUserId(Long userId) {
        return closetRepository.findAllByUserId(userId);
    }

    public Page<Closet> getUserAllCloset(Long userId, Pageable pageable) {
        return closetRepository.getUserAllCloset(userId, pageable);
    }

    public Page<Closet> getUserAllPublicCloset(Long userId, Pageable pageable) {
        return closetRepository.getUserAllPublicCloset(userId, pageable);
    }

    public void saveCloset(Closet closet) {
        closetRepository.save(closet);
    }

    public List<Closet> getRecentAddCloset(Long itemId) {
        return closetRepository.getRecentAddCloset(itemId);
    }
}
