package com.alto.id.bifast.simulator.service.impl;

import com.alto.id.bifast.simulator.domain.Bank;
import com.alto.id.bifast.simulator.repository.BankRepository;
import com.alto.id.bifast.simulator.service.BankService;
import com.alto.id.bifast.simulator.service.dto.BankDTO;
import com.alto.id.bifast.simulator.service.mapper.BankMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.alto.id.bifast.simulator.domain.Bank}.
 */
@Service
@Transactional
public class BankServiceImpl implements BankService {

    private static final Logger LOG = LoggerFactory.getLogger(BankServiceImpl.class);

    private final BankRepository bankRepository;

    private final BankMapper bankMapper;

    public BankServiceImpl(BankRepository bankRepository, BankMapper bankMapper) {
        this.bankRepository = bankRepository;
        this.bankMapper = bankMapper;
    }

    @Override
    public BankDTO save(BankDTO bankDTO) {
        LOG.debug("Request to save Bank : {}", bankDTO);
        Bank bank = bankMapper.toEntity(bankDTO);
        bank = bankRepository.save(bank);
        return bankMapper.toDto(bank);
    }

    @Override
    public BankDTO update(BankDTO bankDTO) {
        LOG.debug("Request to update Bank : {}", bankDTO);
        Bank bank = bankMapper.toEntity(bankDTO);
        bank = bankRepository.save(bank);
        return bankMapper.toDto(bank);
    }

    @Override
    public Optional<BankDTO> partialUpdate(BankDTO bankDTO) {
        LOG.debug("Request to partially update Bank : {}", bankDTO);

        return bankRepository
            .findById(bankDTO.getId())
            .map(existingBank -> {
                bankMapper.partialUpdate(existingBank, bankDTO);

                return existingBank;
            })
            .map(bankRepository::save)
            .map(bankMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BankDTO> findAll() {
        LOG.debug("Request to get all Banks");
        return bankRepository.findAll().stream().map(bankMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<BankDTO> findOne(Long id) {
        LOG.debug("Request to get Bank : {}", id);
        return bankRepository.findById(id).map(bankMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Bank : {}", id);
        bankRepository.deleteById(id);
    }
}
