package br.com.elibrary.infrastructure;

import br.com.elibrary.application.coupon.CouponRepository;
import br.com.elibrary.model.coupon.Coupon;
import br.com.elibrary.util.string.StringUtils;
import jakarta.persistence.NoResultException;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

@Repository
class CouponEntityManagerRepository extends GenericRepository<Coupon, Long> implements CouponRepository {
    private static final Logger LOGGER = Logger.getLogger(CouponEntityManagerRepository.class.getName());

    CouponEntityManagerRepository() {
        super(Coupon.class);
    }

    @Override
    public Optional<Coupon> findByCode(String couponCode) {
        if (StringUtils.isEmpty(couponCode)) {
            return Optional.empty();
        }
        try {
            Coupon code = entityManager.createQuery("SELECT coupon FROM Coupon coupon " +
                            "WHERE UPPER(code) = UPPER(TRIM(:code))", Coupon.class)
                    .setParameter("code", couponCode)
                    .getSingleResult();
            return Optional.ofNullable(code);
        } catch (NoResultException noResultException) {
            LOGGER.log(Level.WARNING, "Coupon with code {0} not found: {1}", new Object[]{couponCode, noResultException.getMessage()});
            return Optional.empty();
        }
    }
}