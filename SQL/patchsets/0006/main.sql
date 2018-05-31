DROP PROCEDURE IF EXISTS transmaster_transport_db.refreshMaterializedView;
CREATE PROCEDURE transmaster_transport_db.`refreshMaterializedView`()
  BEGIN

    TRUNCATE mat_view_row_count_for_user;


    INSERT INTO mat_view_row_count_for_user
    -- MARKET_AGENT
      SELECT r.id, r.role, sum(r.count)
      FROM (
             (SELECT
                requests.marketAgentUserID id,
                users.userRoleID role,
                COUNT(requests.requestID) count
              FROM users
                INNER JOIN requests ON (users.userID = requests.marketAgentUserID)
              GROUP BY requests.marketAgentUserID
              ORDER BY NULL)

             UNION ALL
             -- CLIENT_MANAGER
             (SELECT
                users.userID id,
                users.userRoleID role,
                COUNT(requests.requestID) count
              FROM users
                INNER JOIN requests ON (users.clientID = requests.clientID)
              GROUP BY requests.clientID
              ORDER BY NULL)

             UNION ALL
             -- ADMIN
             (SELECT
                users.userID id,
                users.userRoleID role,
                (SELECT COUNT(requests.requestID) count
                 FROM requests)
              FROM users
              WHERE users.userRoleID = 'ADMIN')) as r
      GROUP BY r.id;

  END;