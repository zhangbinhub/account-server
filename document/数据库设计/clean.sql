/* zt_kernel */
delete from t_online_user;
delete from t_user_role_set where userid in (select userid from zt_accounts.acc_business_account) or userid in (select userid from zt_accounts.acc_cust_account);
delete from T_User_Info where userid in (select userid from zt_accounts.acc_business_account) or userid in (select userid from zt_accounts.acc_cust_account);
delete from T_User_LoginRecord where userid in (select userid from zt_accounts.acc_business_account) or userid in (select userid from zt_accounts.acc_cust_account);
delete from T_User_Configuration where userid in (select userid from zt_accounts.acc_business_account) or userid in (select userid from zt_accounts.acc_cust_account);
delete from t_user where id in (select userid from zt_accounts.acc_business_account) or id in (select userid from zt_accounts.acc_cust_account);

/* zt_accounts */
delete from acc_order_balancepay;
delete from acc_order_balancerefund;
delete from acc_order_balancerecharge;
delete from acc_order_balancetransfer;
delete from acc_order_balancecash;
delete from acc_order_balancesettlement;
delete from acc_order_nonbalancepay;
delete from acc_order_nonbalancerefund;
delete from acc_order_nonbalancetransfer;
delete from acc_order_integralpay;
delete from acc_order_integralrefund;
delete from acc_order_integralrecharge;
delete from acc_order_integralsettlement;
delete from acc_order_currencypay;
delete from acc_order_currencyrefund;
delete from acc_order_currencyrecharge;
delete from acc_order_shopsettlement;
delete from acc_order_normaltrade;
delete from acc_order_internaltrade;
delete from acc_order_reviewrecord;
delete from acc_entry_itemflow;
delete from acc_order_base;
delete from acc_packageInfo_record;

delete from acc_customerid_serial;
delete from acc_cust_bindinfo;
delete from acc_cust_subaccount;
delete from acc_cust_account;
delete from acc_business_bindinfo;
delete from acc_business_inneraccount;
delete from acc_business_subaccount;
delete from acc_business_account;
delete from acc_business_channel;

/* zt_manage */
delete from acc_batch_histables;
delete from acc_batch_situation;
delete from acc_cust_monthstatistics;
delete from acc_cust_yearstatistics;
delete from acc_inner_statistics;
delete from acc_inner_summary;