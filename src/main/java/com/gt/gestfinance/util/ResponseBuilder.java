package com.gt.gestfinance.util;

/**
 * Classe qui contruit de retour des méthodes Rest
 *
 * @author <a href="mailcode:ali.amadou@ace3i.com?">Yasser Ali</a>
 * @since 29/06/2017
 * @version v1
 */
public class ResponseBuilder {

    /**
     * Le constructeur
     */
    private ResponseBuilder() {
        // do something
    }

    /**
     * Renseigne la severity
     *
     * @param severity
     * @return
     */
    public static ISeverity severity(Severity severity) {
        return new ResponseBuilder.Builder(severity);
    }

    /**
     * Renseigne l'info
     *
     * @return
     */
    public static ISeverity info() {
        return new ResponseBuilder.Builder(Severity.INFO);
    }

    /**
     * Renseigne le succes
     *
     * @return
     */
    public static ISeverity success() {
        return new ResponseBuilder.Builder(Severity.SUCCESS);
    }

    /**
     * Renseigne le warn
     *
     * @return
     */
    public static ISeverity warn() {
        return new ResponseBuilder.Builder(Severity.WARN);
    }

    /**
     * Renseigne l'error
     *
     * @return
     */
    public static ISeverity error() {
        return new ResponseBuilder.Builder(Severity.ERROR);
    }

    /**
     * ISeverity
     */
    @FunctionalInterface
    public interface ISeverity {

        /**
         * Le code
         *
         * @param code
         * @return
         */
        ITitle code(String code);
    }

    /**
     * ITitle
     */
    @FunctionalInterface
    public interface ITitle {

        /**
         * Le titre
         *
         * @param title
         * @return
         */
        IMessage title(String title);
    }

    /**
     * IMessage
     */
    @FunctionalInterface
    public interface IMessage {

        /**
         * Le message
         *
         * @param message
         * @return
         */
        IBuild message(String message);
    }

    /**
     * IBuild
     */
    public interface IBuild {

        /**
         * la data
         *
         * @param data
         * @return
         */
        IBuild data(Object data);

        /**
         * build
         *
         * @return
         */
        Response build();

        /**
         * buildI18n
         *
         * @return
         */
        Response buildI18n();
    }

    /**
     * La classe builder
     */
    private static class Builder implements ISeverity, ITitle, IMessage, IBuild {

        /**
         *
         */
        private Response resp;
        /**
         *
         */
        private CustomResourceBundleMessageSource messageSource;

        /**
         * Le constructeur
         *
         * @param severity
         */
        public Builder(Severity severity) {
            resp = new Response();
            resp.setSeverity(severity);
            messageSource = ApplicationContextProvider.getContext()
                    .getBean(CustomResourceBundleMessageSource.class);
        }

        /**
         * code
         *
         * @param code
         * @return
         */
        @Override
        public ITitle code(String code) {
            resp.setCode(code);
            return this;
        }

        /**
         * title
         *
         * @param title
         * @return
         */
        @Override
        public IMessage title(String title) {
            resp.setTitle(title);
            return this;
        }

        /**
         * message
         *
         * @param message
         * @return
         */
        @Override
        public IBuild message(String message) {
            resp.setMessage(message);
            return this;
        }

        /**
         * data
         *
         * @param data
         * @return
         */
        @Override
        public IBuild data(Object data) {
            resp.setData(data);
            return this;
        }

        /**
         * build
         *
         * @return
         */
        @Override
        public Response build() {
            return resp;
        }

        /**
         * Internationnalisation
         *
         * @return
         */
        @Override
        public Response buildI18n() {
            String title = messageSource.getMessage(resp.getTitle());
            String message = messageSource.getMessage(resp.getMessage());

            resp.setTitle(title);
            resp.setMessage(message);

            return resp;
        }

    }
}
