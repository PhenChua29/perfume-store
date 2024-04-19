package Lib;

import Exceptions.*;
import Interfaces.Exceptions.ICustomException;
import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

public class ExceptionUtils {

    private static Map<String, ICustomException> exceptionMap;

    public enum ExceptionType {
        // User info
        WrongPasswordException,
        AccountNotFoundException,
        AccountDeactivatedException,
        PhoneNumberDuplicationException,
        UsernameDuplicationException,
        EmailDuplicationException,
        EmailDoesntExist,
        DefaultDeliveryAddressNotFoundException,
        DefaultDeliveryAddressWillBeNotFoundException,
        // Employee info
        CitizenIDDuplicationException,
        // Brand
        BrandNotFoundException,
        OperationDeleteBrandFailedCauseOfExistedProduct,
        // Product
        ProductNotFoundException,
        ProductDeactivatedException,
        NotEnoughProductQuantityException,
        // Order
        OrderNotFoundException,
        // Other
        NotEnoughInformationException,
        OperationAddFailedException,
        OperationDeleteFailedException,
        OperationEditFailedException,
        InvalidInputException,
        // Voucher
        InvalidVoucherException,
        VoucherNotFoundException,
        NotEnoughVoucherQuantityException,
        NoProductVoucherAppliedException,
        VoucherCodeDuplication,
        //Import
        ImportNotFoundException

    }

    static {
        exceptionMap = new HashMap<String, ICustomException>() {
            {
                // User info
                put(ExceptionType.WrongPasswordException.toString(), new WrongPasswordException());
                put(ExceptionType.AccountNotFoundException.toString(), new AccountNotFoundException());
                put(ExceptionType.AccountDeactivatedException.toString(), new AccountDeactivatedException());
                put(ExceptionType.PhoneNumberDuplicationException.toString(), new PhoneNumberDuplicationException());
                put(ExceptionType.UsernameDuplicationException.toString(), new UsernameDuplicationException());
                put(ExceptionType.EmailDuplicationException.toString(), new EmailDuplicationException());
                put(ExceptionType.EmailDoesntExist.toString(), new EmailDoesntExist());
                put(ExceptionType.DefaultDeliveryAddressNotFoundException.toString(),
                        new DefaultDeliveryAddressNotFoundException());
                put(ExceptionType.DefaultDeliveryAddressWillBeNotFoundException.toString(),
                        new DefaultDeliveryAddressWillBeNotFoundException());
                // Employee info
                put(ExceptionType.CitizenIDDuplicationException.toString(), new CitizenIDDuplicationException());
                // Brand
                put(ExceptionType.BrandNotFoundException.toString(), new BrandNotFoundException());
                put(ExceptionType.OperationDeleteBrandFailedCauseOfExistedProduct.toString(), new OperationDeleteBrandFailedCauseOfExistedProduct());
                // Product
                put(ExceptionType.ProductNotFoundException.toString(), new ProductNotFoundException());
                put(ExceptionType.ProductDeactivatedException.toString(), new ProductDeactivatedException());
                // Order
                put(ExceptionType.OrderNotFoundException.toString(), new OrderNotFoundException());
                // Other
                put(ExceptionType.NotEnoughInformationException.toString(), new NotEnoughInformationException());
                put(ExceptionType.OperationAddFailedException.toString(), new OperationAddFailedException());
                put(ExceptionType.OperationDeleteFailedException.toString(), new OperationDeleteFailedException());
                put(ExceptionType.OperationEditFailedException.toString(), new OperationEditFailedException());
                put(ExceptionType.InvalidInputException.toString(), new InvalidInputException());
                // Voucher
                put(ExceptionType.InvalidVoucherException.toString(), new InvalidVoucherException());
                put(ExceptionType.VoucherNotFoundException.toString(), new VoucherNotFoundException());
                put(ExceptionType.NotEnoughProductQuantityException.toString(),
                        new NotEnoughProductQuantityException());
                put(ExceptionType.NotEnoughVoucherQuantityException.toString(),
                        new NotEnoughProductQuantityException());
                put(ExceptionType.NoProductVoucherAppliedException.toString(), new NoProductVoucherAppliedException());
                put(ExceptionType.VoucherCodeDuplication.toString(), new VoucherCodeDuplication());

                put(ExceptionType.ImportNotFoundException.toString(), new ImportNotFoundException());
            }
        };

    }

    public static String generateExceptionQueryString(HttpServletRequest request) {
        if (request.getAttribute("exceptionType") == null) {
            return "";
        }
        String exceptionKey = (String) request.getAttribute("exceptionType");
        String exception = "?err" + exceptionMap.getOrDefault(exceptionKey, new DefaultException()).getQueryString()
                + "=true";
        return exception;
    }

    // Query String can be "errPNF=true&&otherAttribute=..."
    private static String getExceptionNameByQueryString(String queryString) {
        if (queryString == null) {
            return null;
        }
        // Extract the exception attribute. Example: PNF instead of errPNF
        String attribute = extractExceptionQueryString(queryString);
        if (attribute == null) {
            return null;
        }
        System.out.println("Attribute exception: " + attribute);
        // Iterate through a map entry to compare query string
        for (Map.Entry<String, ICustomException> entry : exceptionMap.entrySet()) {
            if (entry.getValue().getQueryString().equals(attribute)) {
                return entry.getKey();
            }
        }
        // if not, so it is default exception
        return "";
    }

    public static String extractExceptionQueryString(String queryString) {
        if (queryString == null) {
            return null;
        }
        String[] attributes = queryString.split("=");
        if (attributes.length == 0) {
            return null;
        }

        String attribute = attributes[0];
        if (!attribute.startsWith("err")) {
            return null;
        }
        System.out.println("attribute start with err: " + attribute);
        attribute = attribute.substring(3, attribute.length());
        return attribute;
    }

    public static String getMessageFromExceptionQueryString(String queryString) {
        String exceptionKey = getExceptionNameByQueryString(queryString);
        System.out.println("Exception key:" + exceptionKey);
        ICustomException exception = exceptionMap.getOrDefault(exceptionKey, new DefaultException());
        return exception.getMessage();
    }

    // =================================== VALIDATION SECTION =================================
    public static boolean isWebsiteError(String queryString) {
        return getExceptionNameByQueryString(queryString) != null;
    }

    // put(ExceptionType.WrongPasswordException.toString(), new
    // WrongPasswordException());
    // put(ExceptionType.AccountNotFoundException.toString(), new
    // AccountNotFoundException());
    // put(ExceptionType.AccountDeactivatedException.toString(), new
    // AccountDeactivatedException());
    // put(ExceptionType.PhoneNumberDuplicationException.toString(), new
    // PhoneNumberDuplicationException());
    // put(ExceptionType.UsernameDuplicationException.toString(), new
    // UsernameDuplicationException());
    // put(ExceptionType.EmailDuplicationException.toString(), new
    // EmailDuplicationException());
    // //Employee info
    // put(ExceptionType.CitizenIDDuplicationException.toString(), new
    // CitizenIDDuplicationException());
    // //Brand
    // put(ExceptionType.BrandNotFoundException.toString(), new
    // BrandNotFoundException());
    // //Product
    // put(ExceptionType.ProductNotFoundException.toString(), new
    // ProductNotFoundException());
    // put(ExceptionType.ProductDeactivatedException.toString(), new
    // ProductDeactivatedException());
    // //Other
    // put(ExceptionType.NotEnoughInformationException.toString(), new
    // NotEnoughInformationException());
    // put(ExceptionType.OperationAddFailedException.toString(), new
    // OperationAddFailedException());
    // User info
    public static boolean isWrongPassword(String queryString) {
        return getExceptionNameByQueryString(queryString) != null
                && getExceptionNameByQueryString(queryString).equals(ExceptionType.WrongPasswordException.toString());
    }

    public static boolean isAccountNotFound(String queryString) {
        return getExceptionNameByQueryString(queryString) != null
                && getExceptionNameByQueryString(queryString).equals(ExceptionType.AccountNotFoundException.toString());
    }

    public static boolean isAccountDeactivated(String queryString) {
        return getExceptionNameByQueryString(queryString) != null
                && getExceptionNameByQueryString(queryString).equals(ExceptionType.AccountDeactivatedException.toString());
    }

    public static boolean isPhoneNumberDuplication(String queryString) {
        return getExceptionNameByQueryString(queryString) != null
                && getExceptionNameByQueryString(queryString).equals(ExceptionType.PhoneNumberDuplicationException.toString());
    }

    public static boolean isUsernameDuplication(String queryString) {
        return getExceptionNameByQueryString(queryString) != null
                && getExceptionNameByQueryString(queryString).equals(ExceptionType.UsernameDuplicationException.toString());
    }

    public static boolean isEmailDuplication(String queryString) {
        return getExceptionNameByQueryString(queryString) != null
                && getExceptionNameByQueryString(queryString).equals(ExceptionType.EmailDuplicationException.toString());
    }

    public static boolean isDefaultDeliveryAddressNotFound(String queryString) {
        return getExceptionNameByQueryString(queryString) != null
                && getExceptionNameByQueryString(queryString).equals(ExceptionType.DefaultDeliveryAddressNotFoundException.toString());
    }

    public static boolean isDefaultDeliveryAddressWillBeNotFound(String queryString) {
        return getExceptionNameByQueryString(queryString) != null
                && getExceptionNameByQueryString(queryString).equals(ExceptionType.DefaultDeliveryAddressWillBeNotFoundException.toString());
    }
    // Employee info

    public static boolean isCitizenIDDuplication(String queryString) {
        return getExceptionNameByQueryString(queryString) != null
                && getExceptionNameByQueryString(queryString).equals(ExceptionType.CitizenIDDuplicationException.toString());
    }
    // Brand

    public static boolean isBrandNotFound(String queryString) {
        return getExceptionNameByQueryString(queryString) != null
                && getExceptionNameByQueryString(queryString).equals(ExceptionType.BrandNotFoundException.toString());
    }

    public static boolean isOperationDeleteBrandFailedCauseOfExistedProduct(String queryString) {
        return getExceptionNameByQueryString(queryString) != null
                && getExceptionNameByQueryString(queryString).equals(ExceptionType.ProductNotFoundException.toString());
    }

    // Product
    public static boolean isProductNotFound(String queryString) {
        return getExceptionNameByQueryString(queryString) != null
                && getExceptionNameByQueryString(queryString).equals(ExceptionType.ProductNotFoundException.toString());
    }

    public static boolean isProductDeactivated(String queryString) {
        return getExceptionNameByQueryString(queryString) != null
                && getExceptionNameByQueryString(queryString).equals(ExceptionType.ProductDeactivatedException.toString());
    }
    // Other

    public static boolean isNotEnoughInformation(String queryString) {
        return getExceptionNameByQueryString(queryString) != null
                && getExceptionNameByQueryString(queryString)
                        .equals(ExceptionType.NotEnoughInformationException.toString());
    }

    public static boolean isOperationAddFailed(String queryString) {
        return getExceptionNameByQueryString(queryString) != null
                && getExceptionNameByQueryString(queryString).equals(ExceptionType.OperationAddFailedException.toString());
    }

    public static boolean isOperationDeleteFailed(String queryString) {
        return getExceptionNameByQueryString(queryString) != null
                && getExceptionNameByQueryString(queryString)
                        .equals(ExceptionType.OperationDeleteFailedException.toString());
    }

    public static boolean isOperationEditFailed(String queryString) {
        return getExceptionNameByQueryString(queryString) != null
                && getExceptionNameByQueryString(queryString).equals(ExceptionType.OperationEditFailedException.toString());
    }

    public static boolean isInvalidInput(String queryString) {
        return getExceptionNameByQueryString(queryString) != null
                && getExceptionNameByQueryString(queryString).equals(ExceptionType.InvalidInputException.toString());
    }
    // Voucher

    public static boolean isInvalidVoucher(String queryString) {
        return getExceptionNameByQueryString(queryString) != null
                && getExceptionNameByQueryString(queryString).equals(ExceptionType.InvalidVoucherException.toString());
    }

    public static boolean isVoucherNotFound(String queryString) {
        return getExceptionNameByQueryString(queryString) != null
                && getExceptionNameByQueryString(queryString).equals(ExceptionType.VoucherNotFoundException.toString());
    }

    public static boolean NoProductVoucherAppliedException(String queryString) {
        return getExceptionNameByQueryString(queryString) != null
                && getExceptionNameByQueryString(queryString).equals(ExceptionType.NoProductVoucherAppliedException.toString());
    }

    public static boolean isNotEnoughProductQuantityException(String queryString) {
        return getExceptionNameByQueryString(queryString) != null
                && getExceptionNameByQueryString(queryString)
                        .equals(ExceptionType.NotEnoughProductQuantityException.toString());
    }

    public static boolean isNotEnoughVoucherQuantityException(String queryString) {
        return getExceptionNameByQueryString(queryString) != null
                && getExceptionNameByQueryString(queryString)
                        .equals(ExceptionType.NotEnoughVoucherQuantityException.toString());
    }

    public static boolean isVoucherCodeDuplication(String queryString) {
        return getExceptionNameByQueryString(queryString) != null
                && getExceptionNameByQueryString(queryString)
                        .equals(ExceptionType.VoucherCodeDuplication.toString());
    }

    public static boolean isOrderNotFound(String queryString) {
        return getExceptionNameByQueryString(queryString) != null
                && getExceptionNameByQueryString(queryString).equals(ExceptionType.OrderNotFoundException.toString());
    }

    public static boolean isImportNotFound(String queryString) {
        return getExceptionNameByQueryString(queryString) != null
                && getExceptionNameByQueryString(queryString).equals(ExceptionType.ImportNotFoundException.toString());
    }
}