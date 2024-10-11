import React, { useState, useEffect } from "react";
import { Grid, Header, Form, Button, Message } from "semantic-ui-react";
import api from "../../api/api";
import { useParams, useNavigate } from "react-router-dom";

const EditBranchManager = () => {
  const { branchManagerId: id } = useParams();
  const navigate = useNavigate();

  const [managerData, setManagerData] = useState({
    name: "",
    email: "",
    password: "",
    phone: "",
  });
  const [errors, setErrors] = useState({});
  const [loading, setLoading] = useState(false);
  const [success, setSuccess] = useState(false);

  useEffect(() => {
    const fetchManagerData = async () => {
      try {
        const response = await api.get(`/branch-managers/${id}`);
        const { name, email, password, phone } = response.data;
        setManagerData({ name, email, password, phone });
      } catch (error) {
        console.error("Error fetching branch manager:", error);
      }
    };

    fetchManagerData();
  }, [id]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setManagerData({ ...managerData, [name]: value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setSuccess(false);

    // Reset errors
    setErrors({});

    // Validation
    let formIsValid = true;
    const updatedErrors = {};

    if (!managerData.name) {
      updatedErrors.name = "Name is required";
      formIsValid = false;
    }

    if (!managerData.email) {
      updatedErrors.email = "Email is required";
      formIsValid = false;
    }

    if (!managerData.password) {
      updatedErrors.password = "Password is required";
      formIsValid = false;
    }

    if (!managerData.phone) {
      updatedErrors.phone = "Phone is required";
      formIsValid = false;
    }

    if (!formIsValid) {
      setErrors(updatedErrors);
      setLoading(false);
      return;
    }

    // Handle branch manager update logic here
    try {
      await api.put(`/branch-managers/${id}`, managerData);
      setSuccess(true);
      navigate("/admin/branch-managers");
    } catch (error) {
      console.log(error);
    }

    setLoading(false);
  };

  return (
    <Grid textAlign="center" style={{ height: "100vh", marginTop: 80 }}>
      <Grid.Column style={{ maxWidth: 450 }}>
        <Header as="h2" textAlign="center">
          Edit Branch Manager
        </Header>
        <Form size="large" loading={loading} onSubmit={handleSubmit}>
          <Form.Input
            fluid
            placeholder="Name"
            name="name"
            value={managerData.name}
            onChange={handleChange}
            error={errors.name ? true : false}
          />
          {errors.name && (
            <div style={{ color: "red", marginBottom: 8 }}>{errors.name}</div>
          )}

          <Form.Input
            fluid
            placeholder="Email"
            name="email"
            value={managerData.email}
            onChange={handleChange}
            error={errors.email ? true : false}
          />
          {errors.email && (
            <div style={{ color: "red", marginBottom: 8 }}>{errors.email}</div>
          )}

          <Form.Input
            fluid
            placeholder="Password"
            type="password"
            name="password"
            value={managerData.password}
            onChange={handleChange}
            error={errors.password ? true : false}
          />
          {errors.password && (
            <div style={{ color: "red", marginBottom: 8 }}>
              {errors.password}
            </div>
          )}

          <Form.Input
            fluid
            placeholder="Phone"
            name="phone"
            value={managerData.phone}
            onChange={handleChange}
            error={errors.phone ? true : false}
          />
          {errors.phone && (
            <div style={{ color: "red", marginBottom: 8 }}>{errors.phone}</div>
          )}

          <Button fluid color="green" size="large" type="submit">
            Save Changes
          </Button>
        </Form>
        {success && (
          <Message positive>
            <Message.Header>
              Branch Manager updated successfully!
            </Message.Header>
            <p>You can now view the updated details.</p>
          </Message>
        )}
      </Grid.Column>
    </Grid>
  );
};

export default EditBranchManager;
