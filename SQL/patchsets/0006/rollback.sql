DROP PROCEDURE refreshMaterializedView;
CREATE PROCEDURE transmaster_transport_db.refreshMaterializedView()
  BEGIN

    TRUNCATE mat_view_row_count_for_user;

    -- расчет размеров всех результатов для пользователей
    INSERT INTO mat_view_row_count_for_user
    -- MARKET_AGENT
      (SELECT
         requests.marketAgentUserID,
         users.userRoleID,
         COUNT(requests.requestID)
       FROM users
         INNER JOIN requests ON (users.userID = requests.marketAgentUserID)
       GROUP BY requests.marketAgentUserID
       ORDER BY NULL)

      UNION ALL
      -- CLIENT_MANAGER
      (SELECT
         users.userID,
         users.userRoleID,
         COUNT(requests.requestID)
       FROM users
         INNER JOIN requests ON (users.clientID = requests.clientID)
       GROUP BY requests.clientID
       ORDER BY NULL)

      UNION ALL
      -- ADMIN
      (SELECT
         users.userID,
         users.userRoleID,
         (SELECT COUNT(requests.requestID)
          FROM requests)
       FROM users
       WHERE users.userRoleID = 'ADMIN');

  END;
