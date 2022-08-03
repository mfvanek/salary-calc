package com.mfvanek.salary.calc.services;

import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public abstract class AbstractService {
}
