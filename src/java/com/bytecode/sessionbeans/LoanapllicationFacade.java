/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bytecode.sessionbeans;

import com.bytecode.entities.Loanapllication;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Ahmed
 */
@Stateless
public class LoanapllicationFacade extends AbstractFacade<Loanapllication> {
    @PersistenceContext(unitName = "BankingCoreV2PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public LoanapllicationFacade() {
        super(Loanapllication.class);
    }
    
}
