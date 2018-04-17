package com.gt.gestfinance.util;

import com.gt.base.exception.CustomException;
import org.springframework.test.web.servlet.*;

/**
 * MockMvc customisé
 *
 * @author <a href="mailto:claude-rodrigue.affodogandji@ace3i.com?">RODRIGUE
 * AFFODOGANDJI</a>
 */
public class CustomMockMvc {

    /**
     * Une instance de MockMvc
     */
    private final MockMvc restUserMockMvc;

    /**
     * Le constructeur
     *
     * @param restUserMockMvc
     */
    public CustomMockMvc(MockMvc restUserMockMvc) {
        this.restUserMockMvc = restUserMockMvc;
    }

    /**
     * @see
     * MockMvc#perform(RequestBuilder)
     * @param requestBuilder
     * @return
     * @throws CustomException
     */
    public CustomResultActions perform(RequestBuilder requestBuilder) throws CustomException {
        try {
            return new CustomResultActions(restUserMockMvc.perform(requestBuilder));
        } catch (Exception ex) {
            throw new CustomException(ex);
        }
    }

    /**
     * ResultActions customisé
     */
    public class CustomResultActions {

        /**
         * Une instance de ResultActions
         */
        private final ResultActions resultActions;

        /**
         * Le constructeur
         *
         * @param resultActions
         */
        public CustomResultActions(ResultActions resultActions) {
            this.resultActions = resultActions;
        }

        /**
         * @see
         * ResultActions#andExpect(ResultMatcher)
         * @param matcher
         * @return
         * @throws CustomException
         */
        public CustomResultActions andExpect(ResultMatcher matcher) throws CustomException {
            try {
                return new CustomResultActions(resultActions.andExpect(matcher));
            } catch (Exception ex) {
                throw new CustomException(ex);
            }
        }

        /**
         * @see
         * ResultActions#andDo(ResultHandler)
         * @param handler
         * @return
         * @throws CustomException
         */
        public CustomResultActions andDo(ResultHandler handler) throws CustomException {
            try {
                return new CustomResultActions(resultActions.andDo(handler));
            } catch (Exception ex) {
                throw new CustomException(ex);
            }
        }

        /**
         * @see ResultActions#andReturn()
         * @return
         */
        public MvcResult andReturn() {
            return resultActions.andReturn();
        }

    }
}
