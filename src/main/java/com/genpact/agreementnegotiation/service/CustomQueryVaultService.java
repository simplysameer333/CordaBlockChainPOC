package com.genpact.agreementnegotiation.service;

import net.corda.core.node.ServiceHub;
import net.corda.core.node.services.CordaService;
import net.corda.core.serialization.SingletonSerializeAsToken;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@CordaService
public class CustomQueryVaultService extends SingletonSerializeAsToken {
    private ServiceHub serviceHub = null;

    public CustomQueryVaultService(ServiceHub serviceHub) {
        this.serviceHub = serviceHub;
    }

    public List<String> getStateFromListValue(String value) {
        List<String> result = new ArrayList<String>();
        try {
            String query = "SELECT DISTINCT LINEARID"
                    + " FROM IOU_STATES"
                    + " JOIN LIST_COLLECTION"
                    + " WHERE AGREEMENTNEGOTIATIONSCHEMA$PERSISTENTIOU_TRANSACTION_ID  = TRANSACTION_ID"
                    + " AND LIST_COLLECTION.VALUE = ?";

            Connection conn = serviceHub.jdbcSession();
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, value);

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                result.add(rs.getString(1));
            }

            return result;
        } catch (SQLException e) {
            e.printStackTrace();


        }
        return null;
    }
}
